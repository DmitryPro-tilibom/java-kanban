package com.yandex.java_kanban.service;

import com.yandex.java_kanban.model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks;
    private final Map<Integer, Epic> epics;
    private final Map<Integer, SubTask> subTasks;
    private final HistoryManager historyManager;
    private final Comparator<Task> taskComparator = Comparator.comparing(Task::getStartTime);
    private final Set<Task> prioritizedTasks = new TreeSet<>(taskComparator);
    private int taskSequence = 0;

    public InMemoryTaskManager(HistoryManager defaultHistory) {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
    }

    @Override
    public Task createTask(Task task) {
        boolean isNotIntersecting = tasks.values().stream()
                .allMatch(task1 -> timeCheckPassed(task, task1));
        if (isNotIntersecting) {
            task.setId(generateID());
            tasks.put(task.getId(), task);
            if (task.getStartTime() != null) {
                prioritizedTasks.add(task);
            }
        }
        return task;
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        historyManager.addToHistory(task);
        return task;
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteAllTasks() {
        for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
            historyManager.remove(entry.getKey());
        }
        tasks.clear();
    }

    @Override
    public void deleteTaskById(int id) {
        historyManager.remove(id);
        tasks.remove(id);
    }

    @Override
    public void updateTask(Task task) {
        Task updatedTask = tasks.get(task.getId());
        updatedTask.setName(task.getName());
        updatedTask.setDescription(task.getDescription());
        updatedTask.setStatus(task.getStatus());
        updatedTask.setStartTime(task.getStartTime());
        updatedTask.setDuration(task.getDuration());
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(generateID());
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        historyManager.addToHistory(epic);
        return epic;
    }

    @Override
    public List<SubTask> getAllEpicSubTasks(int epicID) {
        return subTasks.values().stream()
                .filter(subTask -> subTask.getEpicID() == epicID)
                .toList();
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteAllEpics() {
        for (Map.Entry<Integer, Epic> entry : epics.entrySet()) {
            historyManager.remove(entry.getKey());
        }
        for (Map.Entry<Integer, SubTask> entry : subTasks.entrySet()) {
            historyManager.remove(entry.getKey());
        }
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void deleteEpicById(int id) {
        historyManager.remove(id);
        final Epic epicToUpdate = epics.remove(id);
        for (int subtaskID : epicToUpdate.getSubtaskIDList()) {
            historyManager.remove(subtaskID);
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
        boolean isNotIntersecting = subTasks.values().stream()
                .allMatch(task -> timeCheckPassed(subTask, task));
        if (isNotIntersecting) {
            subTask.setId(generateID());
        }
        subTasks.put(subTask.getId(), subTask);
        Epic epicToUpdate = epics.get(subTask.getEpicID());
        epicToUpdate.addSubTask(subTask.getId());
        calculateEpicStatus(epicToUpdate.getId());
        calculateEpicTime(epicToUpdate.getId());
        return subTask;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = subTasks.get(id);
        historyManager.addToHistory(subTask);
        return subTask;
    }

    @Override
    public List<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void deleteAllSubTasks() {
        for (Epic epic : epics.values()) {
            epic.deleteAllSubTasks();
            calculateEpicStatus(epic.getId());
            calculateEpicTime(epic.getId());
        }
        for (Map.Entry<Integer, SubTask> entry : subTasks.entrySet()) {
            historyManager.remove(entry.getKey());
        }
        subTasks.clear();
    }

    @Override
    public void deleteSubTaskById(Integer id) {
        final SubTask subTaskToDelete = subTasks.remove(id);
        Epic epicToUpdate = epics.get(subTaskToDelete.getEpicID());
        historyManager.remove(id);
        epicToUpdate.deleteSubTask(id);
        calculateEpicStatus(epicToUpdate.getId());
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        SubTask updatedSubTask = subTasks.get(subTask.getId());
        updatedSubTask.setName(subTask.getName());
        updatedSubTask.setDescription(subTask.getDescription());
        updatedSubTask.setStatus(subTask.getStatus());
        updatedSubTask.setStartTime(subTask.getStartTime());
        updatedSubTask.setDuration(subTask.getDuration());
        calculateEpicStatus(updatedSubTask.getEpicID());
        calculateEpicTime(updatedSubTask.getEpicID());
    }

    @Override
    public void calculateEpicTime(int id) {
        Epic epic = epics.get(id);
        List<SubTask> epicSubTasks = getAllEpicSubTasks(epic.getId());
        LocalDateTime startTime = epicSubTasks.getFirst().getStartTime();
        LocalDateTime endTime = epicSubTasks.getFirst().getEndTime();
        for (SubTask subTask : epicSubTasks) {
            if (subTask.getStartTime().isBefore(startTime)) startTime = subTask.getStartTime();
            if (subTask.getEndTime().isAfter(endTime)) endTime = subTask.getEndTime();
        }
        epic.setStartTime(startTime);
        epic.setEndTime(endTime);
        Duration duration = Duration.between(startTime, endTime);
        epic.setDuration(duration);
    }

    public void calculateEpicStatus(int id) {
        int doneStatusCount = 0;
        int newStatusCount = 0;
        Epic epic = epics.get(id);
        List<Integer> subtaskIDList = epic.getSubtaskIDList();
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

    public boolean timeCheckPassed(Task task1, Task task2) {
        if (task1.getStartTime() == null || task1.getEndTime() == null || task2.getStartTime() == null ||
                task2.getEndTime() == null) return false;
        else if (task1.getStartTime().isBefore(task2.getStartTime()) &&
                task1.getEndTime().isBefore(task2.getStartTime())) return true;
        else if (task1.getStartTime().isAfter(task2.getEndTime()) &&
                task1.getEndTime().isAfter(task2.getEndTime())) return true;
        else return task1.getStartTime().equals(task2.getStartTime()) ||
                    task1.getStartTime().equals(task2.getEndTime()) ||
                    task1.getEndTime().equals(task2.getStartTime()) ||
                    task1.getEndTime().equals(task2.getEndTime());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public void getPrioritizedTasks() {
        for (Task task : prioritizedTasks) {
            System.out.println(task);
        }
    }

    private int generateID() {
        return ++taskSequence;
    }
}