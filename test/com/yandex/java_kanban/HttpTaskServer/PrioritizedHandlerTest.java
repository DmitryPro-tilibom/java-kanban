package com.yandex.java_kanban.HttpTaskServer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yandex.java_kanban.model.Status;
import com.yandex.java_kanban.model.Task;
import com.yandex.java_kanban.service.InMemoryTaskManager;
import com.yandex.java_kanban.service.TaskManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PrioritizedHandlerTest {
    TaskManager taskManager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(taskManager);
    Gson gson = HttpTaskServer.getGson();

    public PrioritizedHandlerTest() throws IOException {}

    @BeforeEach
    public void setUp() {
        taskManager.deleteAllTasks();
        taskManager.deleteAllSubTasks();
        taskManager.deleteAllEpics();
        taskServer.startConnection();
    }

    @AfterEach
    public  void shutDown() {
        taskServer.stopConnection();
        System.out.println("Соединение разорвано");
    }

    @Test
    public void shouldReturnPrioritizedTaskList() throws IOException, InterruptedException {
        Task task1 = new Task("task1", "test task", Status.NEW,
                LocalDateTime.now().plusMinutes(20), Duration.ofMinutes(2));
        Task task2 = new Task("task2", "test task", Status.NEW,
                LocalDateTime.now().plusMinutes(30), Duration.ofMinutes(3));
        Task task3 = new Task("task3", "test task", Status.NEW,
                LocalDateTime.now().plusMinutes(40), Duration.ofMinutes(4));

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);

        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .GET()
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        Type taskListType = new TypeToken<List<Task>>(){}.getType();
        List<Task> prioritizedTasks = gson.fromJson(httpResponse.body(), taskListType);
        assertEquals(200, httpResponse.statusCode());
        assertNotNull(prioritizedTasks, "Список задач пуст");
        assertEquals(3, prioritizedTasks.size(), "некорректное число задач.");
    }
}
