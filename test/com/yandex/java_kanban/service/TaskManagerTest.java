package com.yandex.java_kanban.service;

import com.yandex.java_kanban.model.Epic;
import com.yandex.java_kanban.model.Status;
import com.yandex.java_kanban.model.SubTask;
import com.yandex.java_kanban.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    protected Task createTask() {
        return new Task("task1", "testTask", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(2));
    }

    protected Epic createEpic() {
        return new Epic("epic", "testEpic", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(2));
    }

    protected SubTask createSubTask(Epic epic) {
        return new SubTask("subTask1", "testSubtask", Status.NEW, LocalDateTime.now(),
                Duration.ofMinutes(2), epic.getId());
    }
}
