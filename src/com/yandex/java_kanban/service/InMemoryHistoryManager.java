package com.yandex.java_kanban.service;

import com.yandex.java_kanban.model.Task;

import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager{
    private final LinkedList<Task> history = new LinkedList<>();
    @Override
    public void add(Task task) {
        if (history.size() == 10) {
            history.remove();
        }
       history.add(task);
    }

    @Override
    public LinkedList<Task> getHistory() {
        return history;
    }
}
