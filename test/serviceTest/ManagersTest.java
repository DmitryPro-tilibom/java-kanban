package serviceTest;

import com.yandex.java_kanban.service.HistoryManager;
import com.yandex.java_kanban.service.Managers;
import com.yandex.java_kanban.service.TaskManager;
import org.junit.Test;

import com.yandex.java_kanban.model.Status;
import com.yandex.java_kanban.model.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ManagersTest {
    private final TaskManager taskManager = Managers.getDefault();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    private final List<Task> history = historyManager.getHistory();
    Task task = new Task("task", "testtask", Status.NEW);

    @Test
    public void taskManagerCreationTest() {
        assertNotNull(taskManager, "менеджер задач не проинициализирован.");
    }

    @Test
    public void historyManagerCreationTest() {
        assertNotNull(historyManager, "менеджер истории не проинициализирован.");
    }

    @Test
    public void historyIsNotNullTest() {
        assertNotNull(history, "истории задач нет.");
    }

    @Test
    public void taskIsAddedToHistoryTest() {
        historyManager.addToHistory(task);
        assertEquals(1, historyManager.getHistory().size(), "задача не добавлена в историю.");
    }

    @Test
    public void historySizeMustBe10Test() {
        for (int i = 0; i < 12; i++) {
            historyManager.addToHistory(task);
        }
        assertEquals(10, historyManager.getHistory().size(), "Размер не 10.");
    }
}
