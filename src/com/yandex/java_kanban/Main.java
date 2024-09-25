package com.yandex.java_kanban;

import com.yandex.java_kanban.model.Epic;
import com.yandex.java_kanban.model.Status;
import com.yandex.java_kanban.model.SubTask;
import com.yandex.java_kanban.model.Task;
import com.yandex.java_kanban.service.TaskManager;

public class Main {
    public static void main(String[] args) {
        TaskManager tm = new TaskManager();
        Task task1 = tm.createTask(new Task("Подъем", "Встать в 7 утра", Status.NEW));
        tm.updateTask(task1);
        System.out.println(tm.getTaskById(1));
        Task task2 = tm.createTask(new Task("Отбой", "Лечь в 11 вечера", Status.NEW));
        System.out.println(tm.showAllTasks());

        Epic epic1 = tm.createEpic(new Epic("Зарядка", "Отжимания и подтягивания"));
        Epic epic2 = tm.createEpic(new Epic("Прогулка", "Пройтись по району"));
        System.out.println(tm.showAllEpics());
        tm.updateEpic(epic1);
        System.out.println(tm.getEpicById(3));

        SubTask subTask1 = tm.createSubTask(new SubTask("Отжимания", "30 раз",
                Status.NEW, 3));
        SubTask subTask2 = tm.createSubTask(new SubTask("Подтягивания", "10 раз",
                Status.NEW, 3));
        System.out.println(tm.getAllEpicSubTasks(3));
        SubTask subTask3 = tm.createSubTask(new SubTask("Пройтись по району", "1000 шагов",
                Status.NEW, 4));
        System.out.println(tm.getAllEpicSubTasks(4));
        tm.updateSubTask(subTask1);
        System.out.println(tm.getSubTaskById(5));
        System.out.println(tm.getEpicById(3));
        tm.updateSubTask(subTask2);
        System.out.println(tm.getSubTaskById(6));
        System.out.println(tm.getEpicById(3));
        tm.deleteAllSubTasks();
        System.out.println(tm.getEpicById(3));
    }
}
