package com.yandex.java_kanban;

import com.yandex.java_kanban.model.Epic;
import com.yandex.java_kanban.model.Status;
import com.yandex.java_kanban.model.SubTask;
import com.yandex.java_kanban.model.Task;
import com.yandex.java_kanban.service.*;

public class Main {
    public static void main(String[] args) {
        TaskManager tm = Managers.getDefault();
        Task task1 = tm.createTask(new Task("Подъем", "Встать в 7 утра", Status.NEW));
        tm.updateTask(task1);
        Task task2 = tm.createTask(new Task("Отбой", "Лечь в 11 вечера", Status.NEW));
        Epic epic1 = tm.createEpic(new Epic("Зарядка", "Отжимания и подтягивания"));
        Epic epic2 = tm.createEpic(new Epic("Прогулка", "Пройтись по району"));
        tm.updateEpic(epic1);
        SubTask subTask1 = tm.createSubTask(new SubTask("Отжимания", "30 раз",
                Status.NEW, 3));
        SubTask subTask2 = tm.createSubTask(new SubTask("Подтягивания", "10 раз",
                Status.NEW, 3));
        SubTask subTask3 = tm.createSubTask(new SubTask("Пройтись по району", "1000 шагов",
                Status.NEW, 4));
        tm.updateSubTask(subTask1);

        System.out.println(tm.getTaskById(1));
        System.out.println(tm.getTaskById(2));
        System.out.println(tm.getEpicById(3));
        System.out.println(tm.getEpicById(4));
        System.out.println(tm.getSubTaskById(5));
        System.out.println(tm.getSubTaskById(6));
        System.out.println(tm.getSubTaskById(7));

        System.out.println("History size " + tm.getHistory().size());
        for (Task element : tm.getHistory()) {
            System.out.println(element);
        }
        tm.deleteAllEpics();

        System.out.println("History size " + tm.getHistory().size());
        for (Task element : tm.getHistory()) {
            System.out.println(element);
        }
    }
}
