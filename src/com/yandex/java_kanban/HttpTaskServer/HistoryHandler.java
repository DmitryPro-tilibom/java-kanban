package com.yandex.java_kanban.HttpTaskServer;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.java_kanban.model.Task;
import com.yandex.java_kanban.service.TaskManager;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = HttpTaskServer.getGson();

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        List<Task> history = taskManager.getHistory();
        if (history.isEmpty()) {
            sendNotFound(httpExchange, "История просмотров задач пуста.");
        }
        writeResponse(httpExchange, gson.toJson(history));
    }
}
