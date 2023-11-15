package test;

import controllers.managers.InMemoryHistoryManager;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class InMemoryHistoryManagerTest {

    private InMemoryHistoryManager manager;

    @BeforeEach
    public void setup() {
        manager = new InMemoryHistoryManager();
    }

    @Test
    public void testAddTask() {
        Task task = new Task("test", "test", 1);
        manager.add(task);
        List<Task> history = manager.getHistory();
        assertEquals(task, history.get(0));
    }

    @Test
    public void testRemoveTask() {
        Task task1 = new Task("test", "test", 1);
        Task task2 = new Task("test", "test", 2);
        manager.add(task1);
        manager.add(task2);
        manager.remove(1);
        List<Task> history = manager.getHistory();
        assertTrue(!history.isEmpty());
        assertEquals(task2, history.get(0));
    }

    @Test
    public void testHistory() {
        Task task1 = new Task("test1", "test", 1);
        Task task2 = new Task("test1", "test", 2);
        manager.add(task1);
        manager.add(task2);
        System.out.println(manager.getHistory());
        List<Task> history = manager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
    }

    @Test
    public void testAddSameTaskTwice() {
        Task task = new Task("test", "test", 1);
        manager.add(task);
        manager.add(task);
        List<Task> history = manager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task, history.get(0));
    }

}