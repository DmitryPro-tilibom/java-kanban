import com.yandex.java_kanban.model.Status;
import com.yandex.java_kanban.model.Task;
import com.yandex.java_kanban.service.HistoryManager;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

import java.util.LinkedList;

import static com.yandex.java_kanban.service.Managers.getDefaultHistory;
import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {
    private final HistoryManager historyManager = getDefaultHistory();
    private final LinkedList<Task> history = historyManager.getHistory();

    @Test
    public void classManagersAddInMemoryHistoryManager() {
        assertNotNull(historyManager, "Экземпляр класса не пронициализирован");
    }

    @Test
    public void addTaskToHistory() {
        historyManager.add(new Task("AAA", "TASK", Status.NEW));
        assertNotNull(history, "История не пустая.");
        Assertions.assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    public void historySizeMustBe10() {
        for (int i = 0; i < 11; i++) {
            historyManager.add(new Task("AAA", "TASK", Status.NEW));
        }
        assertEquals(10, history.size(), "Размер не равен 10.");
    }
}
