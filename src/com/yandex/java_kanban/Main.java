package com.yandex.java_kanban;

import com.yandex.java_kanban.model.Epic;
import com.yandex.java_kanban.model.Status;
import com.yandex.java_kanban.model.SubTask;
import com.yandex.java_kanban.model.Task;
import com.yandex.java_kanban.service.TaskManager;

public class Main {
    public static void main(String[] args) {
        TaskManager tm = new TaskManager();
        tm.createTask(new Task("Подъем", "Встать в 7 утра", Status.NEW));
        tm.createTask(new Task("Отбой", "Лечь в 11 вечера", Status.NEW));
        System.out.println(tm.showAllTasks());
        tm.updateTask(1, new Task("Подъем", "Встать в 8 утра", Status.DONE));
        System.out.println(tm.showAllTasks());

        tm.createEpic(new Epic("Зарядка", "Отжимания и подтягивания"));
        tm.createEpic(new Epic("Прогулка", "Пройтись по району"));
        System.out.println(tm.showAllEpics());
        tm.updateEpic(3, new Epic("Зарядка", "Больше отжиманий и подтягиваний"));
        System.out.println(tm.showAllEpics());

        tm.createSubTask(new SubTask("Отжимания", "30 раз", Status.NEW, 3));
        tm.createSubTask(new SubTask("Подтягивания", "10 раз", Status.NEW, 3));
        System.out.println(tm.getAllEpicSubTasks(3));
        tm.createSubTask(new SubTask("Пройтись по району", "1000 шагов", Status.NEW, 4));
        System.out.println(tm.getAllEpicSubTasks(4));
        tm.updateSubTask(5, new SubTask("Отжимания", "40 раз", Status.DONE, 3));
        System.out.println(tm.getSubTaskById(5));
        System.out.println(tm.getEpicById(3));
        tm.updateSubTask(6, new SubTask("Подтягивания", "10 раз", Status.DONE, 3));
        System.out.println(tm.getSubTaskById(6));
        System.out.println(tm.getEpicById(3));
        tm.deleteAllSubTasks();
        System.out.println(tm.getEpicById(3));
    }
}
