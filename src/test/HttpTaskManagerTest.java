package test;

import controllers.KV.KVServer;
import controllers.http.HttpTaskServer;
import controllers.managers.HttpTaskManager;
import controllers.managers.Managers;
import model.Task;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class HttpTaskManagerTest {
    static KVServer kvServer;
    HttpTaskServer httpTaskServer = new HttpTaskServer();

    public HttpTaskManagerTest() throws IOException {
    }

    @BeforeAll
    public static void startKV() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
    }
    @BeforeEach
    public void setup() throws IOException {
        httpTaskServer.start();
    }

    @AfterEach
    public void stopSetup() {
        httpTaskServer.stop();
    }
    @AfterAll
    public static void stopKV() {
        kvServer.stop();
    }


    @Test
    public void saveAndLoadTest() throws  IOException {
        HttpTaskManager saveManager = (HttpTaskManager) Managers.getDefault();
        Task task1 = new Task("task1 title", "task1 description", LocalDateTime.now(), 60);
        saveManager.addTask(task1);
        HttpTaskManager loadManager = new HttpTaskManager("http://localhost:8078/");
        ArrayList<Task> history = new ArrayList<>(loadManager.getHistoryManager().getHistory());
        ArrayList<Task> historySecond= new ArrayList<>(loadManager.getPrioritizedTasks());

        Assertions.assertTrue(history.isEmpty());
        Assertions.assertTrue(historySecond.isEmpty());
    }

}
