import Model.Status;
import Model.Task;
import Service.TaskManager;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();
        taskManager.createTask(new Task("4th sprint", "pass all tests", Status.NEW));
        taskManager.showAllTasks();
    }
}
