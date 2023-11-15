package test;

import controllers.managers.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;


class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    void init() {
        taskManager = new InMemoryTaskManager();
        time = LocalDateTime.now();
    }


}