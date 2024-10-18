import com.yandex.java_kanban.service.InMemoryTaskManager;
import com.yandex.java_kanban.service.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryTaskManagerTest {
    private InMemoryTaskManager taskManager;

    @BeforeEach
    public void addTaskTest() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void classManagersAddInMemoryTaskManager() {
        assertNotNull(taskManager, "Экземпляр класса не пронициализирован");
    }

}
