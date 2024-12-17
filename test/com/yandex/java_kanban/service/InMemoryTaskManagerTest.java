package com.yandex.java_kanban.service;

import com.yandex.java_kanban.model.Epic;
import com.yandex.java_kanban.model.SubTask;
import com.yandex.java_kanban.model.Status;
import com.yandex.java_kanban.model.Task;
import com.yandex.java_kanban.service.HistoryManager;
import com.yandex.java_kanban.service.TaskManager;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static com.yandex.java_kanban.service.Managers.getDefault;
import static com.yandex.java_kanban.service.Managers.getDefaultHistory;
import static org.junit.Assert.*;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager>{
    private final TaskManager taskManager = getDefault();
    Task testTask = new Task("AAA", "TASK", Status.NEW);
    Epic epic1 = new Epic("EPIC1", "TESTEPIC");
    //SubTask subTask1 = new SubTask("subTask1", "testsubtask", Status.NEW, epic1.getId());
    SubTask subTask2 = new SubTask("subTask2", "testsubtask", Status.DONE, epic1.getId());

    @Test
    public void tasksEqualById() {
        taskManager.createTask(testTask);
        assertEquals(testTask, taskManager.getTaskById(testTask.getId()));
    }

    @Test
    public void epicsEqualById() {
        Epic testEpic = new Epic("EPIC", "TESTEPIC");
        taskManager.createEpic(testEpic);
        assertEquals(testEpic, taskManager.getEpicById(testEpic.getId()));
    }

    @Test
    public void subTasksEqualById() {
        taskManager.createEpic(epic1);
        SubTask testSubTask = new SubTask("AAA", "SUBTASK", Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(0), epic1.getId());
        taskManager.createSubTask(testSubTask);
        assertEquals(testSubTask, taskManager.getSubTaskById(testSubTask.getId()));
    }

    @Test
    public void epicCantBeItsOwnEpic() {
        taskManager.createEpic(epic1);
        assertFalse(epic1.getSubtaskIDList().contains(epic1.getId()));
    }

    @Test
    public void subTaskCantBeItsOwnEpic() {
        taskManager.createEpic(epic1);
        SubTask subTask = new SubTask("AAA", "SUBTASK", Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(0), epic1.getId());
        taskManager.createSubTask(subTask);
        assertNotEquals(subTask.getId(), subTask.getEpicID());
    }

    @Test
    public void epicChangesStatusIfSubTaskAdded() {
        taskManager.createEpic(epic1);
        SubTask subTask1 = new SubTask("AAA", "SUBTASK", Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(0), epic1.getId());
        taskManager.createSubTask(subTask1);
        assertSame(epic1.getStatus(), Status.NEW);
        SubTask subTask2 = new SubTask("BBB", "SUBTASK", Status.DONE,
                LocalDateTime.now(), Duration.ofMinutes(0), epic1.getId());
        taskManager.createSubTask(subTask2);
        assertSame(Status.IN_PROGRESS, epic1.getStatus());
        subTask1.setStatus(Status.DONE);
        taskManager.calculateEpicStatus(epic1.getId());
        assertSame(Status.DONE, epic1.getStatus());
    }

    @Test
    public void epicRemovesSubtaskAfterSubtaskDelete() {
        taskManager.createEpic(epic1);
        SubTask subTask1 = new SubTask("AAA", "SUBTASK", Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(0), epic1.getId());
        taskManager.createSubTask(subTask1);
        assertTrue(epic1.getSubtaskIDList().contains(subTask1.getId()));
        taskManager.deleteSubTaskById(subTask1.getId());
        assertTrue(epic1.getSubtaskIDList().isEmpty());
    }

    @Test
    public void subTaskChangesIdAfterDeleteAndCreated() {
        taskManager.createEpic(epic1);
        SubTask subTask1 = new SubTask("AAA", "SUBTASK", Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(0), epic1.getId());
        taskManager.createSubTask(subTask1);
        int oldId = subTask1.getId();
        taskManager.deleteSubTaskById(subTask1.getId());
        taskManager.createSubTask(subTask1);
        int newId = subTask1.getId();
        assertNotEquals(oldId, newId);
    }

    @Test
    public void epicTimeCalculationTest() {
        taskManager.createEpic(epic1);
        SubTask subTask1 = new SubTask("AAA", "SUBTASK", Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(0), epic1.getId());
        taskManager.createSubTask(subTask1);
        taskManager.calculateEpicTime(epic1.getId());
        assertEquals(epic1.getStartTime(), subTask1.getStartTime());
    }
}
