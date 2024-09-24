package com.yandex.java_kanban.model;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subtaskIDList = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
    }

    public ArrayList<Integer> getSubtaskIDList() {
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
