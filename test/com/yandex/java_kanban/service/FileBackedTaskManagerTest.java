package com.yandex.java_kanban.service;

import com.yandex.java_kanban.model.Epic;
import com.yandex.java_kanban.model.Status;
import com.yandex.java_kanban.model.SubTask;
import com.yandex.java_kanban.model.Task;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

public class FileBackedTaskManagerTest extends TaskManagerTest<InMemoryTaskManager>{
    File file = File.createTempFile("test", ".CSV");
    File fileForException = new File("C:");
    FileBackedTaskManager savedFilesManager = new FileBackedTaskManager(file);
    FileBackedTaskManager loadedFilesManager = new FileBackedTaskManager(file);
    FileBackedTaskManager forExceptionTestFilesManager = new FileBackedTaskManager(fileForException);

    Task task = new Task("task1", "test", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(2));
    Epic epic = new Epic("epic1", "test", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(2));
    SubTask subTask = new SubTask("subTask1", "test", Status.NEW,
            LocalDateTime.now(), Duration.ofMinutes(3),2);

    public FileBackedTaskManagerTest() throws IOException {
    }

    @Test
    public void taskIsSavedToFile() {
        savedFilesManager.createTask(task);
        loadedFilesManager.loadFromFile();
        assertEquals(List.of(task), loadedFilesManager.getTasks());
    }

    @Test
    public void removedTaskIsNotSaved() {
        savedFilesManager.createTask(task);
        savedFilesManager.deleteTaskById(1);
        loadedFilesManager.loadFromFile();
        assertTrue(loadedFilesManager.getTasks().isEmpty());
    }

    @Test
    public void allTypesOfTasksSavedAndLoaded() {
        savedFilesManager.createTask(task);
        savedFilesManager.createEpic(epic);
        savedFilesManager.createSubTask(subTask);
        loadedFilesManager.loadFromFile();
        assertEquals(savedFilesManager.getTasks(), loadedFilesManager.getTasks());
        assertEquals(savedFilesManager.getEpics(), loadedFilesManager.getEpics());
        assertEquals(savedFilesManager.getSubTasks(), loadedFilesManager.getSubTasks());
    }

    @Test
    public void testException() {
        assertThrows(ManagerSaveException.class, () -> forExceptionTestFilesManager.createTask(task));
    }
}
