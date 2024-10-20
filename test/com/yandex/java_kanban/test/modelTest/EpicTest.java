package com.yandex.java_kanban.test.modelTest;

import com.yandex.java_kanban.model.Epic;
import com.yandex.java_kanban.service.Managers;
import com.yandex.java_kanban.service.TaskManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class EpicTest {
    TaskManager taskManager = Managers.getDefault();
    Epic epic1 = new Epic("epic1", "epicOne");
    Epic epic2 = new Epic("epic2", "epicTwo");


    @Test
    public void idShouldNotBeEqual() {
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        assertNotEquals(epic1.getId(), epic2.getId(), "Идентификаторы эпиков равны.");
    }
}
