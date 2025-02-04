package com.yandex.java_kanban.HttpTaskServer;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.java_kanban.model.Task;
import com.yandex.java_kanban.service.TaskManager;

import java.io.IOException;
import java.util.List;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = HttpTaskServer.getGson();

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();
        switch (method) {
            case "GET":
                if (path.matches("/tasks")) {
                    getTasks(httpExchange);
                } else {
                    getTaskById(httpExchange);
                }
                break;
            case "POST":
                sendOrUpdateTask(httpExchange);
                break;
            case "DELETE":
                if (path.matches("tasks/")) {
                    deleteTasks(httpExchange);
                } else {
                    deleteTaskById(httpExchange);
                }
                break;
            default:
                sendNotFound(httpExchange, "Неверный адрес");
        }
        httpExchange.close();
    }

    private void getTasks(HttpExchange httpExchange) throws IOException {
        List<Task> tasks = taskManager.getTasks();
        sendText(httpExchange, gson.toJson(tasks));
    }

    private void getTaskById(HttpExchange httpExchange) throws IOException {
        int id = getTaskId(httpExchange.getRequestURI().getPath());
        Task task = taskManager.getTaskById(id);
        if (task == null) {
            sendNotFound(httpExchange, "Задача не найдена");
            return;
        }
        sendText(httpExchange, gson.toJson(task));
    }

    private void sendOrUpdateTask(HttpExchange httpExchange) throws IOException {
        String body = new String(httpExchange.getRequestBody().readAllBytes());
        Task task = gson.fromJson(body, Task.class);
        if (task.getId() == 0) {
            taskManager.createTask(task);
            sendText(httpExchange, gson.toJson(task));
        } else {
            try {
                taskManager.updateTask(task);
                sendText(httpExchange, "Задача обновлена.");
            } catch (Exception e) {
                sendNotFound(httpExchange, "Задача не найдена");
            }
        }
    }

    private void deleteTasks(HttpExchange httpExchange) throws IOException {
        taskManager.deleteAllTasks();
        sendText(httpExchange, "Задачи удалены.");
    }

    private  void deleteTaskById(HttpExchange httpExchange) throws IOException {
        int id = getTaskId(httpExchange.getRequestURI().getPath());
        taskManager.deleteTaskById(id);
        sendText(httpExchange, "Задача удалена.");
    }
}
