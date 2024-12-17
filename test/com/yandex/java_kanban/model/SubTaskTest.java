package com.yandex.java_kanban.model;

import com.yandex.java_kanban.model.Epic;
import com.yandex.java_kanban.model.Status;
import com.yandex.java_kanban.model.SubTask;
import com.yandex.java_kanban.service.Managers;
import com.yandex.java_kanban.service.TaskManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SubTaskTest {
    TaskManager taskManager = Managers.getDefault();

    Epic epic = new Epic("epic", "epic");
    Epic epicToTest = taskManager.createEpic(epic);
    SubTask subTask1 = new SubTask("subtask1", "subtaskone", Status.NEW, 
                                   LocalDateTime.now(), Duration.ofMinutes(2), epicToTest.getId());
    SubTask subTask2 = new SubTask("subtask2", "subtasktwo", Status.DONE, 
                                   LocalDateTime.now(), Duration.ofMinutes(3), epicToTest.getId());


    @Test
    public void idShouldNotBeEqual() {
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        assertSame(epicToTest.getStatus(), Status.IN_PROGRESS, "Статус эпика неверен.");
        assertNotEquals(subTask1.getId(), subTask2.getId(), "Идентификаторы подзадач равны.");
    }
}
