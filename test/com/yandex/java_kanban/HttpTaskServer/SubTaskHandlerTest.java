package com.yandex.java_kanban.HttpTaskServer;

import com.google.gson.Gson;
import com.yandex.java_kanban.model.Epic;
import com.yandex.java_kanban.model.Status;
import com.yandex.java_kanban.model.SubTask;
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

public class SubTaskHandlerTest {
    TaskManager taskManager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(taskManager);
    Gson gson = HttpTaskServer.getGson();

    public SubTaskHandlerTest() throws IOException {}

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
        System.out.println("Соединение разорвано");
    }

    @Test
    public void addSubTaskTest() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "test epic");
        taskManager.createEpic(epic);
        LocalDateTime start = LocalDateTime.now().plusMinutes(10);
        Duration duration = Duration.ofMinutes(5);
        SubTask subTask1 = new SubTask("subtask1", "test subtask", Status.NEW, start, duration, 1);
        taskManager.createSubTask(subTask1);
        String subTaskToJson = gson.toJson(subTask1);

        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/SubTasks");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(subTaskToJson))
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, httpResponse.statusCode());
        List<SubTask> subtasks = taskManager.getSubTasks();
        assertNotNull(subtasks, "Список подзадач пуст.");
        assertEquals(1, subtasks.size(), "Некорректное количество подзадач");
        assertEquals("subtask1", subtasks.getFirst().getName(), "Некорректное название подзадачи");
        assertEquals(1, epic.getSubtaskIDList().size(), "Число подзадач эпика некорректно");
    }
}
