package com.yandex.java_kanban.test.serviceTest;

import com.yandex.java_kanban.model.Status;
import com.yandex.java_kanban.model.Task;
import com.yandex.java_kanban.service.HistoryManager;
import org.junit.Test;

import static com.yandex.java_kanban.service.Managers.getDefaultHistory;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryHistoryManagerTest {
    private final HistoryManager historyManager = getDefaultHistory();
    Task task = new Task("task", "testtask", Status.NEW);

    @Test
    public void taskIsAddedToHistoryTest() {
        historyManager.addToHistory(task);
        assertEquals(1, historyManager.getHistory().size(), "задача не добавлена в историю.");
    }

    @Test
    public void historySizeMustBe10Test() {
        for (int i = 0; i < 12; i++) {
            historyManager.addToHistory(task);
        }
        assertEquals(10, historyManager.getHistory().size(), "Размер не 10.");
    }
}
