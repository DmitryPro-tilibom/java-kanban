package com.yandex.java_kanban.service;

import com.yandex.java_kanban.model.Epic;
import com.yandex.java_kanban.model.Status;
import com.yandex.java_kanban.model.SubTask;
import com.yandex.java_kanban.model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, SubTask> subTasks;
    private int taskSequence = 0;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
    }

    public Task createTask(Task task) {
        task.setId(generateID());
        tasks.put(task.getId(), task);
        return task;
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public ArrayList<Task> showAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public void updateTask(Task task) {
        Task updatedTask = tasks.get(task.getId());
        updatedTask.setName(task.getName());
        updatedTask.setDescription(task.getDescription());
        updatedTask.setStatus(task.getStatus());
    }

    public Epic createEpic(Epic epic) {
        epic.setId(generateID());
        epics.put(epic.getId(), epic);
        return epic;
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public ArrayList<Integer> getAllEpicSubTasks(int epicID) {
        return epics.get(epicID).getSubtaskIDList();
    }

    public ArrayList<Epic> showAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    public void deleteEpicById(int id) {
        final Epic epicToUpdate = epics.remove(id);
        for (int subtaskID : epicToUpdate.getSubtaskIDList()) {
            subTasks.remove(subtaskID);
        }
    }

    public void updateEpic(Epic epic) {
        Epic updatedEpic = epics.get(epic.getId());
        updatedEpic.setName(epic.getName());
        updatedEpic.setDescription(epic.getDescription());
    }

    public SubTask createSubTask(SubTask subTask) {
        subTask.setId(generateID());
        subTasks.put(subTask.getId(), subTask);
        Epic epicToUpdate = epics.get(subTask.getEpicID());
        epicToUpdate.addSubTask(subTask.getId());
        calculateEpicStatus(epicToUpdate.getId());
        return subTask;
    }

    public SubTask getSubTaskById(int id) {
        return subTasks.get(id);
    }

    public ArrayList<SubTask> showAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public void deleteAllSubTasks() {
        for (Epic epic : epics.values()) {
            epic.deleteAllSubTasks();
            calculateEpicStatus(epic.getId());
        }
        subTasks.clear();
    }

    public void deleteSubTaskById(Integer id) {
        final SubTask subTaskToDelete = subTasks.remove(id);
        Epic epicToUpdate = subTaskToDelete.getEpic();
        epicToUpdate.deleteSubTask(id);
        calculateEpicStatus(epicToUpdate.getId());
    }

    public void updateSubTask(SubTask subTask) {
        SubTask updatedSubTask = subTasks.get(subTask.getId());
        updatedSubTask.setName(subTask.getName());
        updatedSubTask.setDescription(subTask.getDescription());
        updatedSubTask.setStatus(subTask.getStatus());
        calculateEpicStatus(updatedSubTask.getEpicID());
    }

    private int generateID() {
        return ++taskSequence;
    }

    private void calculateEpicStatus(int id) {
        int doneStatusCount = 0;
        int newStatusCount = 0;
        Epic epic = epics.get(id);
        ArrayList<Integer> subtaskIDList = epic.getSubtaskIDList();
        if (subtaskIDList.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        for (Integer subTaskID : subtaskIDList) {
            final Status status = subTasks.get(subTaskID).getStatus();
            if (status == Status.NEW) {
                newStatusCount++;
            } else if (status == Status.DONE) {
                doneStatusCount++;
            } else {
                epic.setStatus(Status.IN_PROGRESS);
                return;
            }
        }
        if (doneStatusCount == subtaskIDList.size()) {
            epic.setStatus(Status.DONE);
        } else if (newStatusCount == subtaskIDList.size()) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}
