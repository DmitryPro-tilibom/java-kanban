package com.yandex.java_kanban.HttpTaskServer;

import com.google.gson.Gson;
import com.yandex.java_kanban.model.Status;
import com.yandex.java_kanban.model.Task;
import com.yandex.java_kanban.service.InMemoryTaskManager;
import com.yandex.java_kanban.service.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TaskHandlerTest {
    TaskManager taskManager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(taskManager);
    Gson gson = HttpTaskServer.getGson();

    public TaskHandlerTest() throws IOException {}

    @BeforeEach
    public void setUp() {
        taskManager.deleteAllTasks();
        taskManager.deleteAllSubTasks();
        taskManager.deleteAllEpics();
        taskServer.startConnection();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stopConnection();
    }

    @Test
    public void addTaskTest() throws IOException, InterruptedException {
        LocalDateTime start = LocalDateTime.now();
        Duration duration = Duration.ofMinutes(5);
        Task task1 = new Task("task1", "test task", Status.NEW, start, duration);
        taskManager.createTask(task1);
        String taskToJson = gson.toJson(task1);

        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskToJson))
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, httpResponse.statusCode());
        List<Task> tasks = taskManager.getTasks();
        assertNotNull(tasks, "Список задач пуст.");
        assertEquals(1, tasks.size(), "Некорректное количество задач");
        assertEquals("task1", tasks.getFirst().getName(), "Некорректное название задачи");
    }
}
