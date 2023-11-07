package test;

import controllers.FileBackedTasksManager;
import controllers.InMemoryHistoryManager;
import controllers.InMemoryTaskManager;
import exceptions.ManagerSaveException;
import exceptions.TimeCrossingException;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import test.TaskManagerTest;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
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

//    @Test
//    void testToString() {
//        Task task = new Task(1, TaskType.TASK, "Task 1", Status.NEW, "Test task", time, 60);
//        String expected = "1,TASK,Task 1,NEW,Test task,\n";
//        String actual = taskManager.toString(task);
//        assertEquals(expected, actual);
//    }

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

//    @Test
//    void testFromString() {
//        String taskStr = "1,TASK,test,NEW,test,2023.11.08 | 00.54,180,";
//        String epicStr = "1,EPIC,test,NEW,test,2023.11.08 | 00.54,180,";
//        String subTaskStr = "4,SUBTASK,test,NEW,test,2023.11.08 | 02.54,60,1";
//
//        var expectedTask = new Task(1, TaskType.TASK, "Task 1", Status.NEW, "Test task", LocalDateTime.parse("2023.11.08 | 00.54,180,", FO));
//        var expectedEpic = new Epic(2, TaskType.EPIC, "Epic 1", Status.NEW, "Epic task");
//        var expectedSubtask = new SubTask(5, TaskType.SUBTASK, "SubTask for epic with id 2", Status.NEW, "mega subtask", 2 );
//        var actualTask = taskManager.fromString(taskStr);
//        var actualEpic = taskManager.fromString(epicStr);
//        var actualSubtask = taskManager.fromString(subTaskStr);
//        assertNotNull(actualTask);
//        assertNotNull(actualEpic);
//        assertNotNull(actualSubtask);
//        assertEquals(expectedTask, actualTask);
//        assertEquals(expectedEpic, actualEpic);
//        assertEquals(expectedSubtask, actualSubtask);
//    }

    @Test
    void testLoadFromFileWithNoExistingFile() {
        assertDoesNotThrow(() -> {
            taskManager = FileBackedTasksManager.loadFromFile(FILE);
            assertNotNull(taskManager);
        });
    }



//    @Test
//    void testSave() throws IOException, TimeCrossingException {
//        FileBackedTasksManager taskManager = new FileBackedTasksManager();
//        Task task = new Task(1, TaskType.TASK, "Task 1", Status.NEW, "Test task", time, 60);
//        taskManager.addTask(task);
//        assertDoesNotThrow(taskManager::save);
//        String expected = taskManager.toString(task);
//        String acutal = Files.readString(Path.of("resources/config.csv"));
//        var sb = new StringBuilder(acutal);
//        sb.delete(0, "id,type,name,status,description,epic\n".length() - 1);
//        assertEquals(expected.substring(0, expected.length() - 1), (sb.substring(0, expected.length() - 1)));
//    }



}