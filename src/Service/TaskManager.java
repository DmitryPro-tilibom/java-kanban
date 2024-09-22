package Service;

import Model.Epic;
import Model.Status;
import Model.SubTask;
import Model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, SubTask> subTasks;
    private int taskSequence = 0;

    private int generateID() {
        return ++taskSequence;
    }

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
        tasks.put(task.getId(), task);
    }

    public Epic createEpic(Epic epic) {
        epic.setId(generateID());
        epic.setStatus(Status.NEW);
        epics.put(epic.getId(), epic);
        return epic;
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public ArrayList<Epic> showAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    public void deleteEpicById(int id) {
        epics.remove(id);
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        calculateEpicStatus(epic);
    }

    public SubTask createSubTask(SubTask subTask) {
        subTask.setId(generateID());
        subTasks.put(subTask.getId(), subTask);
        return subTask;
    }

    public Task getSubTaskById(int id) {
        return subTasks.get(id);
    }

    public ArrayList<SubTask> showAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public void deleteAllSubTasks() {
        subTasks.clear();
    }

    public void deleteSubTaskById(int id) {
        subTasks.remove(id);
    }

    public void updateSubTask(SubTask subTask) {
        tasks.put(subTask.getId(), subTask);
        calculateEpicStatus(subTask.getEpic());
    }

    private void calculateEpicStatus(Epic epic) {
        int doneStatusCount = 0;
        int newStatusCount = 0;
        ArrayList<SubTask> collection = epic.getSubTasks();
        for (SubTask subTask : collection) {
            if (subTask.getStatus() == Status.NEW) {
                newStatusCount++;
            }
            if (subTask.getStatus() == Status.DONE) {
                doneStatusCount++;
            }
        }
        if (doneStatusCount == collection.size()) {
            epic.setStatus(Status.DONE);
        } else if (newStatusCount == collection.size()) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}
