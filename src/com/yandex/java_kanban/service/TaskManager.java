package com.yandex.java_kanban.service;

import com.yandex.java_kanban.model.Epic;
import com.yandex.java_kanban.model.SubTask;
import com.yandex.java_kanban.model.Task;

import java.util.List;

public interface TaskManager {
    List<Task> showAllTasks();

    List<Epic> showAllEpics();

    List<SubTask> showAllSubTasks();

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubTasks();

    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubTaskById(Integer id);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void calculateEpicStatus(int id);

    void updateSubTask(SubTask subtask);

    Task createTask(Task task);

    Epic createEpic(Epic epic);

    SubTask createSubTask(SubTask subtask);

    SubTask getSubTaskById(int id);

    List<Integer> getAllEpicSubTasks(int epicID);

    Epic getEpicById(int id);

    Task getTaskById(int id);

    List<Task> getHistory();
}
