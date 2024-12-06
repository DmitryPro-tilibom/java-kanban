package com.yandex.java_kanban.service;

import com.yandex.java_kanban.model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private File file;
    private static final String FILE_HEADER = "id,type,name,description,status,epicId(for subtasks)\n";

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public void save() {
        try (FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8, false)) {
            fileWriter.write(FILE_HEADER);
            for (Task task : super.getTasks()) {
                fileWriter.write(toString(task) + "\n");
            }
            for (Epic epic : super.getEpics()) {
                fileWriter.write(toString(epic) + "\n");
            }
            for (SubTask subTask : super.getSubTasks()) {
                fileWriter.write(toString(subTask) + "\n");
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось сохранить данные");
        }
    }

    public void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String string = null;
            ArrayList<String> strings = new ArrayList<>();
            while (reader.ready()) {
                string = reader.readLine();
                if (string.isEmpty()) {
                    break;
                }
                strings.add(string);
            }

            for (int i = 1; i < strings.size(); i++) {
                Task task = fromString(strings.get(i));
                if (task instanceof Epic epic) {
                    addEpic(epic);
                } else if (task instanceof SubTask subTask) {
                    addSubTask(subTask);
                } else {
                    addTask(task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось считать данные");
        }
    }

    public Task addTask(Task task) {
        return super.createTask(task);
    }

    public Epic addEpic(Epic epic) {
        return super.createEpic(epic);
    }

    public SubTask addSubTask(SubTask subtask) {
        return super.createSubTask(subtask);
    }

    @Override
    public Task createTask(Task task) {
        super.createTask(task);
        save();
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        super.createEpic(epic);
        save();
        return epic;
    }

    @Override
    public SubTask createSubTask(SubTask subtask) {
        super.createSubTask(subtask);
        save();
        return subtask;
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubTaskById(Integer id) {
        super.deleteSubTaskById(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subtask = super.getSubTaskById(id);
        save();
        return subtask;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    private String toString(Task task) {
        String[] arrayOfFields = {Integer.toString(task.getId()), getType(task).toString(),
                task.getName(), task.getDescription(), task.getStatus().toString(), getEpicId(task)};
        return String.join(",", arrayOfFields);
    }

    private String getEpicId(Task task) {
        if (task instanceof SubTask) {
            return Integer.toString(((SubTask) task).getEpicID());
        }
        return "";
    }

    private Task fromString(String string) {
        String[] arrayOfFields = string.split(",");
        int id = Integer.parseInt(arrayOfFields[0]);
        String type = arrayOfFields[1];
        String name = arrayOfFields[2];
        String description = arrayOfFields[3];
        Status status = Status.valueOf(arrayOfFields[4].toUpperCase());
        Integer epicId = type.equals("SUBTASK") ? Integer.parseInt(arrayOfFields[5]) : null;

        if (type.equals("EPIC")) {
            Epic epic = new Epic(name, description);
            epic.setId(id);
            epic.setStatus(status);
            return epic;
        } else if (type.equals("SUBTASK")) {
            SubTask subTask = new SubTask(name, description, status, epicId);
            subTask.setId(id);
            return subTask;
        } else {
            Task task = new Task(name, description, status);
            task.setId(id);
            return task;
        }
    }

    private TaskType getType(Task task) {
        if (task instanceof Epic) {
            return TaskType.EPIC;
        } else if (task instanceof SubTask) {
            return TaskType.SUBTASK;
        } else {
            return TaskType.TASK;
        }
    }
}
