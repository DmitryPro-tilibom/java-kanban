package modelTest;

import com.yandex.java_kanban.model.Status;
import com.yandex.java_kanban.model.Task;
import com.yandex.java_kanban.service.Managers;
import com.yandex.java_kanban.service.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TaskTest {
    TaskManager taskManager = Managers.getDefault();
    Task task1 = new Task("taskOne", "testtask1", Status.NEW);
    Task task2 = new Task("taskTw0", "testtask2", Status.NEW);


    @Test
    public void idShouldNotBeEqual() {
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        assertNotEquals(task1.getId(), task2.getId(), "Идентификаторы задач равны.");
    }
}
