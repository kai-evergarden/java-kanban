package test;

import controllers.InMemoryTaskManager;
import exceptions.TimeCrossingException;
import model.Epic;
import model.Status;
import model.SubTask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static model.Status.*;

class EpicTest {
    private Epic epic;
    private SubTask subTask, subTask1, subTask2;
    private InMemoryTaskManager inMemoryTaskManager;


    @Test
    void statusShouldBeNewWhenSubTasksListIsEmpty() {
        inMemoryTaskManager = new InMemoryTaskManager();
        epic = new Epic("Test", "Test");
        inMemoryTaskManager.addEpic(epic);
        Assertions.assertEquals(NEW, epic.getStatus());
    }

    @Test
    void statusShouldBeNewWhenAllSubTasksAreNew() throws TimeCrossingException {
        setUp();
        Assertions.assertEquals(NEW, epic.getStatus());
    }

    @Test
    void statusShouldBeDoneWhenAllSubTasksAreDone() throws TimeCrossingException {
        setUp();
        changeStatusToDone(DONE, subTask,subTask1, subTask2);
        Assertions.assertEquals(DONE, epic.getStatus());
    }

    @Test
    void statusShouldBeInProgressWhenASubTaskIsDone() throws TimeCrossingException {
        setUp();
        changeStatus(subTask, DONE);
        Assertions.assertEquals(IN_PROGRESS, epic.getStatus());
    }

    @Test
    void statusShouldBeInProgressWhenAllSubTasksAreInProgress() throws TimeCrossingException {
        setUp();
        changeStatusToDone(IN_PROGRESS, subTask1, subTask2);
        Assertions.assertEquals(IN_PROGRESS, epic.getStatus());
    }

    private void changeStatusToDone(Status status, SubTask... subtasks) {
        for (SubTask subtask : subtasks) {
            changeStatus(subtask, status);
        }
    }

    private void changeStatus(SubTask subTask, Status status) {
        inMemoryTaskManager.changeSubTaskStatus(subTask, status);
    }

    private void setUp() throws TimeCrossingException {
        inMemoryTaskManager = new InMemoryTaskManager();
        epic = new Epic("Test", "Test");
        inMemoryTaskManager.addEpic(epic);
        subTask = new SubTask("test", "test", epic.getId(), LocalDateTime.now(), 60);
        subTask1 = new SubTask("test", "test", epic.getId(), LocalDateTime.now().plusHours(1), 60);
        subTask2 = new SubTask("test", "test", epic.getId(), LocalDateTime.now().plusHours(1), 60);
        inMemoryTaskManager.addSubTask(subTask);
        inMemoryTaskManager.addSubTask(subTask1);
        inMemoryTaskManager.addSubTask(subTask2);
    }
}