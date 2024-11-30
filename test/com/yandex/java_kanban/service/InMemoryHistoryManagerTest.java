package com.yandex.java_kanban.service;

import com.yandex.java_kanban.model.Epic;
import com.yandex.java_kanban.model.Status;
import com.yandex.java_kanban.model.SubTask;
import com.yandex.java_kanban.model.Task;
import org.junit.Test;

import static com.yandex.java_kanban.service.Managers.getDefault;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryHistoryManagerTest {
    private final TaskManager taskManager = getDefault();
    Task task = new Task("task", "testtask", Status.NEW);
    Epic epic = new Epic("epic", "testepic");

    SubTask subTask1 = new SubTask("subtask1", "subtask1", Status.NEW, 1);

    @Test
    public void taskIsAddedToHistoryTest() {
        taskManager.createTask(task);
        taskManager.getTaskById(1);
        assertEquals(1, taskManager.getHistory().size(), "задача не добавлена в историю.");
    }

    @Test
    public void taskIsRemovedFromHistoryTest() {
        taskManager.createTask(task);
        taskManager.getTaskById(1);
        taskManager.deleteTaskById(1);
        assertEquals(0, taskManager.getHistory().size(), "задача не удалена из истории.");
    }

    @Test
    public void epicIsAddedToHistoryTest() {
        taskManager.createEpic(epic);
        taskManager.getEpicById(1);
        assertEquals(1, taskManager.getHistory().size(), "Эпик не добавлен в историю.");
    }

    @Test
    public void epicIsRemovedFromHistoryTest() {
        taskManager.createEpic(epic);
        taskManager.getEpicById(1);
        taskManager.deleteEpicById(1);
        assertEquals(0, taskManager.getHistory().size(), "Эпик не удален из истории.");
    }

    @Test
    public void subTaskIsAddedToHistoryTest() {
        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask1);
        taskManager.getSubTaskById(2);
        assertEquals(1, taskManager.getHistory().size(), "подзадача не добавлена в историю.");
    }

    @Test
    public void subTaskIsRemovedFromHistoryTest() {
        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask1);
        taskManager.getSubTaskById(2);
        taskManager.deleteSubTaskById(2);
        assertEquals(0, taskManager.getHistory().size(), "подзадача не удалена из истории.");
    }

    @Test
    public void epicIsRemovedFromHistoryWithItsSubTaskTest(){
        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask1);
        taskManager.getEpicById(1);
        taskManager.getSubTaskById(2);
        taskManager.deleteEpicById(1);
        assertEquals(0, taskManager.getHistory().size(), "Подзадача эпика не удалена из истории.");
    }
}
