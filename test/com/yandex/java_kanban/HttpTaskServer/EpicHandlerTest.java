package com.yandex.java_kanban.HttpTaskServer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yandex.java_kanban.model.Epic;
import com.yandex.java_kanban.model.Status;
import com.yandex.java_kanban.model.SubTask;
import com.yandex.java_kanban.service.InMemoryTaskManager;
import com.yandex.java_kanban.service.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

public class EpicHandlerTest {
    TaskManager taskManager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(taskManager);
    Gson gson = HttpTaskServer.getGson();

    public EpicHandlerTest() throws IOException {}

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
        Epic epic = new Epic("epic", "test epic");
        taskManager.createEpic(epic);
        LocalDateTime start = LocalDateTime.now();
        Duration duration = Duration.ofMinutes(5);
        SubTask subTask = new SubTask("subtask", "test subtask", Status.NEW, start, duration, 1);
        taskManager.createSubTask(subTask);
        String epicToJson = gson.toJson(epic);

        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(epicToJson))
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, httpResponse.statusCode());
        List<Epic> epics = taskManager.getEpics();
        assertNotNull(epics, "Список эпиков пуст.");
        assertEquals(1, epics.size(), "Некорректное количество эпиков");
        assertEquals("epic", epics.getFirst().getName(), "Некорректное название эпика");
    }

    @Test
    public void checkEpicSubtasksTest() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "test epic");
        taskManager.createEpic(epic);
        LocalDateTime start = LocalDateTime.now();
        Duration duration = Duration.ofMinutes(5);
        SubTask subTask = new SubTask("subtask", "test subtask", Status.NEW, start, duration, 1);
        taskManager.createSubTask(subTask);

        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1/subtasks");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .GET()
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        Type subTaskListType = new TypeToken<List<SubTask>>(){}.getType();
        List<SubTask> epicSubtasks = gson.fromJson(httpResponse.body(), subTaskListType);
        assertEquals(1, epicSubtasks.size());
        assertEquals(subTask.getEpicID(), epic.getId());
    }
}
