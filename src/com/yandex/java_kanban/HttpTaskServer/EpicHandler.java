package com.yandex.java_kanban.HttpTaskServer;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.java_kanban.model.Epic;
import com.yandex.java_kanban.model.SubTask;
import com.yandex.java_kanban.service.TaskManager;

import java.io.IOException;
import java.util.List;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = HttpTaskServer.getGson();

    public EpicHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();
        switch (method) {
            case "GET":
                if (path.matches("/epics")) {
                    getEpics(httpExchange);
                } else if (path.matches("/epics/\\d/subtasks")) {
                    getEpicSubTasks(httpExchange);
                } else {
                    getEpicById(httpExchange);
                }
                break;
            case "POST":
                sendOrUpdateEpic(httpExchange);
                break;
            case "DELETE":
                if (path.matches("epics/")) {
                    deleteEpics(httpExchange);
                } else {
                    deleteEpicById(httpExchange);
                }
                break;
            default:
                sendNotFound(httpExchange, "Неверный адрес");
        }
        httpExchange.close();
    }

    private void getEpics(HttpExchange httpExchange) throws IOException {
        List<Epic> epics = taskManager.getEpics();
        sendText(httpExchange, gson.toJson(epics));
    }

    private void getEpicSubTasks(HttpExchange httpExchange) throws IOException {
        int id = getTaskId(httpExchange.getRequestURI().getPath());
        Epic epic = taskManager.getEpicById(id);
        if (epic == null) {
            sendNotFound(httpExchange, "Эпик не найден.");
            return;
        }
        List<SubTask> subTasks = taskManager.getAllEpicSubTasks(id);
        sendText(httpExchange, gson.toJson(subTasks));
    }

    private void getEpicById(HttpExchange httpExchange) throws IOException {
        int id = getTaskId(httpExchange.getRequestURI().getPath());
        Epic epic = taskManager.getEpicById(id);
        if (epic == null) {
            sendNotFound(httpExchange, "Эпик не найден");
            return;
        }
        sendText(httpExchange, gson.toJson(epic));
    }

    private void sendOrUpdateEpic(HttpExchange httpExchange) throws IOException {
        String body = new String(httpExchange.getRequestBody().readAllBytes());
        Epic epic = gson.fromJson(body, Epic.class);
        if (epic.getId() == 0) {
            taskManager.createEpic(epic);
            sendText(httpExchange, gson.toJson(epic));
        } else {
            try {
                taskManager.updateEpic(epic);
                sendText(httpExchange, "Эпик обновлен.");
            } catch (Exception e) {
                sendNotFound(httpExchange, "Эпик не найден.");
            }
        }
    }

    private void deleteEpics(HttpExchange httpExchange) throws IOException {
        try {
            taskManager.deleteAllEpics();
            sendText(httpExchange, "Эпики удалены.");
        } catch (Exception e) {
            sendNotFound(httpExchange, "Эпики не найдены.");
        }
    }

    private  void deleteEpicById(HttpExchange httpExchange) throws IOException {
        int id = getTaskId(httpExchange.getRequestURI().getPath());
        Epic epic = taskManager.getEpicById(id);
        if (epic == null) {
            sendNotFound(httpExchange, "Эпик не найден");
        }
        taskManager.deleteEpicById(id);
        sendText(httpExchange, "Эпик удален.");
    }
}
