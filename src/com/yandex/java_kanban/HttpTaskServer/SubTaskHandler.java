package com.yandex.java_kanban.HttpTaskServer;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.java_kanban.model.SubTask;
import com.yandex.java_kanban.service.TaskManager;

import java.io.IOException;
import java.util.List;

public class SubTaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = HttpTaskServer.getGson();

    public SubTaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();
        switch (method) {
            case "GET":
                if (path.matches("/subtasks")) {
                    getSubTasks(httpExchange);
                } else {
                    getSubTaskById(httpExchange);
                }
                break;
            case "POST":
                sendOrUpdateSubTask(httpExchange);
                break;
            case "DELETE":
                if (path.matches("subtasks/")) {
                    deleteSubTasks(httpExchange);
                } else {
                    deleteSubTaskById(httpExchange);
                }
                break;
            default:
                sendNotFound(httpExchange, "Неверный адрес");
        }
        httpExchange.close();
    }

    private void getSubTasks(HttpExchange httpExchange) throws IOException {
        List<SubTask> subTasks = taskManager.getSubTasks();
        sendText(httpExchange, gson.toJson(subTasks));
    }

    private void getSubTaskById(HttpExchange httpExchange) throws IOException {
        int id = getTaskId(httpExchange.getRequestURI().getPath());
        SubTask subTask = taskManager.getSubTaskById(id);
        if (subTask == null) {
            sendNotFound(httpExchange, "Подзадача не найдена");
            return;
        }
        sendText(httpExchange, gson.toJson(subTask));
    }

    private void sendOrUpdateSubTask(HttpExchange httpExchange) throws IOException {
        String body = new String(httpExchange.getRequestBody().readAllBytes());
        SubTask subTask = gson.fromJson(body, SubTask.class);
        if (subTask.getId() == 0) {
            taskManager.createSubTask(subTask);
            sendText(httpExchange, gson.toJson(subTask));
        } else {
            try {
                taskManager.updateSubTask(subTask);
                sendText(httpExchange, "Подзадача обновлена.");
            } catch (Exception e) {
                sendNotFound(httpExchange, "Подзадача не найдена");
            }
        }
    }

    private void deleteSubTasks(HttpExchange httpExchange) throws IOException {
        try {
            taskManager.deleteAllSubTasks();
            sendText(httpExchange, "Подзадачи удалены.");
        } catch (Exception e) {
            sendNotFound(httpExchange, "Подзадачи не найдены.");
        }
    }

    private  void deleteSubTaskById(HttpExchange httpExchange) throws IOException {
        int id = getTaskId(httpExchange.getRequestURI().getPath())
        taskManager.deleteSubTaskById(id);
        sendText(httpExchange, "Подзадача удалена.");
    }
}
