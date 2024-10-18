package com.yandex.java_kanban.service;

import com.yandex.java_kanban.model.Epic;
import com.yandex.java_kanban.model.Status;
import com.yandex.java_kanban.model.SubTask;
import com.yandex.java_kanban.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, SubTask> subTasks;
    //private final LinkedList<Task> history;
    private final HistoryManager historyManager;
    private int taskSequence = 0;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
    }

    @Override
    public Task createTask(Task task) {
        task.setId(generateID());
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public ArrayList<Task> showAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    @Override
    public void updateTask(Task task) {
        Task updatedTask = tasks.get(task.getId());
        updatedTask.setName(task.getName());
        updatedTask.setDescription(task.getDescription());
        updatedTask.setStatus(task.getStatus());
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(generateID());
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public ArrayList<Integer> getAllEpicSubTasks(int epicID) {
        return epics.get(epicID).getSubtaskIDList();
    }

    @Override
    public ArrayList<Epic> showAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void deleteEpicById(int id) {
        final Epic epicToUpdate = epics.remove(id);
        for (int subtaskID : epicToUpdate.getSubtaskIDList()) {
            subTasks.remove(subtaskID);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic updatedEpic = epics.get(epic.getId());
        updatedEpic.setName(epic.getName());
        updatedEpic.setDescription(epic.getDescription());
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        subTask.setId(generateID());
        subTasks.put(subTask.getId(), subTask);
        Epic epicToUpdate = epics.get(subTask.getEpicID());
        epicToUpdate.addSubTask(subTask.getId());
        calculateEpicStatus(epicToUpdate.getId());
        return subTask;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        historyManager.add(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public ArrayList<SubTask> showAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void deleteAllSubTasks() {
        for (Epic epic : epics.values()) {
            epic.deleteAllSubTasks();
            calculateEpicStatus(epic.getId());
        }
        subTasks.clear();
    }

    @Override
    public void deleteSubTaskById(Integer id) {
        final SubTask subTaskToDelete = subTasks.remove(id);
        Epic epicToUpdate = epics.get(subTaskToDelete.getEpicID());
        epicToUpdate.deleteSubTask(id);
        calculateEpicStatus(epicToUpdate.getId());
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        SubTask updatedSubTask = subTasks.get(subTask.getId());
        updatedSubTask.setName(subTask.getName());
        updatedSubTask.setDescription(subTask.getDescription());
        updatedSubTask.setStatus(subTask.getStatus());
        calculateEpicStatus(updatedSubTask.getEpicID());
    }

    @Override
    public void calculateEpicStatus(int id) {
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

    @Override
    public LinkedList<Task> getHistory() {
        return historyManager.getHistory();
    }

    private int generateID() {
        return ++taskSequence;
    }
}
