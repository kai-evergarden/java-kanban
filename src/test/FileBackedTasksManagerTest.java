package test;

import controllers.managers.FileBackedTasksManager;
import controllers.managers.InMemoryHistoryManager;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    public static final String FILE = new File(System.getProperty("user.dir")) +
            File.separator + "resources" + File.separator + "config.csv";

    @BeforeEach
    void init() {
        taskManager = new FileBackedTasksManager();
        time = LocalDateTime.now();
    }


    @Test
    void testHistoryToString() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        historyManager.add(new Task(1, TaskType.TASK, "Task 1", Status.NEW, "Test task", time, 60));
        historyManager.add(new Task(2, TaskType.TASK, "Task 2", Status.NEW, "Test task", time.plusHours(1), 60));
        String expected = " 1,2,";
        String actual = taskManager.historyToString(historyManager);
        assertTrue(actual.equals(expected));
    }

    @Test
    void testHistoryFromString() {
        List<Integer> expected = Arrays.asList(1, 2, 3);
        List<Integer> actual = taskManager.historyFromString("1,2,3");
        assertEquals(expected, actual);
    }


    @Test
    void testLoadFromFileWithNoExistingFile() {
        assertDoesNotThrow(() -> {
            taskManager = FileBackedTasksManager.loadFromFile(FILE);
            assertNotNull(taskManager);
        });
    }


}