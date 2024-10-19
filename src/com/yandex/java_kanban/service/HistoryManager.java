package com.yandex.java_kanban.service;

import com.yandex.java_kanban.model.Task;

import java.util.List;

public interface HistoryManager {
    void addToHistory(Task task);

    List<Task> getHistory();
}
