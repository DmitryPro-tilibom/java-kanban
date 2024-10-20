package com.yandex.java_kanban.service;

import com.yandex.java_kanban.model.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int historySize = 10;
    private final List<Task> history = new LinkedList<>();

    @Override
    public void addToHistory(Task task) {
        if (task == null) {
            System.out.println("Задача не найдена.");
        }
        if (history.size() == historySize) {
            history.removeFirst();
        }
        history.add(task);

    }

    @Override
    public List<Task> getHistory() {
        return List.copyOf(history);
    }
}
