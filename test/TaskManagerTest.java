import com.yandex.java_kanban.model.Epic;
import com.yandex.java_kanban.model.SubTask;
import com.yandex.java_kanban.model.Status;
import com.yandex.java_kanban.model.Task;
import com.yandex.java_kanban.service.TaskManager;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import static com.yandex.java_kanban.service.Managers.getDefault;
import static org.junit.Assert.*;

public class TaskManagerTest {
    private final TaskManager taskManager = getDefault();
    Task testTask = new Task("AAA", "TASK", Status.NEW);
    Epic epic1 = new Epic("EPIC1", "TESTEPIC");

    @Test
    public void tasksEqualById() {
        taskManager.createTask(testTask);
        assertEquals(testTask, taskManager.getTaskById(testTask.getId()));
    }

    @Test
    public void epicsEqualById() {
        Epic testEpic = new Epic("EPIC", "TESTEPIC");
        taskManager.createEpic(testEpic);
        assertEquals(testEpic, taskManager.getEpicById(testEpic.getId()));
    }

    @Test
    public void subTasksEqualById() {
        taskManager.createEpic(epic1);
        SubTask testSubTask = new SubTask("AAA", "SUBTASK", Status.NEW, epic1.getId());
        taskManager.createSubTask(testSubTask);
        assertEquals(testSubTask, taskManager.getSubTaskById(testSubTask.getId()));
    }

    @Test
    public void epicCantBeItsOwnEpic() {
        taskManager.createEpic(epic1);
        assertFalse(epic1.getSubtaskIDList().contains(epic1.getId()));
    }

    @Test
    public void subTaskCantBeItsOwnEpic() {
        taskManager.createEpic(epic1);
        SubTask subTask = new SubTask("testSubTask", "TESTSUBTASK", Status.NEW, epic1.getId());
        taskManager.createSubTask(subTask);
        assertNotEquals(subTask.getId(), subTask.getEpicID());
    }
}
