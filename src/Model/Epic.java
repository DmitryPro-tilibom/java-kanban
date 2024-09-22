package Model;

import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<SubTask> subtasks = new ArrayList<>();

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    public ArrayList<SubTask> getSubTasks() {
        return subtasks;
    }

    public void addSubTask(SubTask subTask) {
        subtasks.add(subTask);
    }

    public void deleteAllSubTasks(SubTask subTask) {
        subtasks.clear();
    }

    public void deleteSubTask(SubTask subTask) {
        subtasks.remove(subTask);
    }
}
