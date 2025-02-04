package com.yandex.java_kanban.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private LocalDateTime endTime;
    private final List<Integer> subtaskIDList = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
    }

    public Epic(String name, String description, Status status, LocalDateTime startTime, Duration duration) {
        super(name, description, status, startTime, duration);
    }

    public List<Integer> getSubtaskIDList() {
        return subtaskIDList;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
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
