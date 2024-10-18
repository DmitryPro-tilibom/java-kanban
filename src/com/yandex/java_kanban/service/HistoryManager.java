package com.yandex.java_kanban.service;

import com.yandex.java_kanban.model.Task;

import java.util.LinkedList;

public interface HistoryManager {
    void add(Task task);

    LinkedList<Task> getHistory();
}
