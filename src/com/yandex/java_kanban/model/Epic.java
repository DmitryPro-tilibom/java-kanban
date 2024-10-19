package com.yandex.java_kanban.model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subtaskIDList = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
    }

    public List<Integer> getSubtaskIDList() {
        return subtaskIDList;
    }

    public void addSubTask(Integer subTaskId) {
        subtaskIDList.add(subTaskId);
    }

    public void deleteAllSubTasks() {
        subtaskIDList.clear();
    }

    public void deleteSubTask(Integer id) {
        subtaskIDList.remove(id);
    }
}
