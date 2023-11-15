package test;

import controllers.interfaces.TaskManager;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    protected LocalDateTime time;

    @Test
    void addTask() {
        var task = new Task("test", "test", time, 60);
        taskManager.addTask(task);
        var saveTask = taskManager.getTaskById(task.getId());
        Assertions.assertNotNull(saveTask, "Task not found");
        Assertions.assertEquals(task, saveTask, "Tasks not equals");
        var tasks = taskManager.getListOfTasks();
        Assertions.assertNotNull(tasks, "The list is null");
        Assertions.assertEquals(1, tasks.size(), "Incorrect number of tasks");
        Assertions.assertEquals(task, tasks.get(0), "Incorrect task in list");
    }

    @Test
    void addEpic() {
        var epic = new Epic("test", "test");
        taskManager.addEpic(epic);
        var saveEpic = taskManager.getEpicById(epic.getId());
        Assertions.assertNotNull(saveEpic, "Epic not found");
        Assertions.assertEquals(epic, saveEpic, "Epics not equals");
        var epics = taskManager.getListOfEpics();
        Assertions.assertNotNull(epics, "The list is null");
        Assertions.assertEquals(1, epics.size(), "Incorrect number of tasks");
        Assertions.assertEquals(epic, epics.get(0), "Incorrect epic in list");
    }

    @Test
    void addSubTask() {
        var epic = new Epic("test", "test");
        taskManager.addEpic(epic);
        var subTask = new SubTask("test", "test", epic.getId(), time, 60);
        taskManager.addSubTask(subTask);

        var saveSubTask = taskManager.getSubtaskById(subTask.getId());
        Assertions.assertNotNull(saveSubTask, "Sub task not found");
        Assertions.assertEquals(subTask, saveSubTask, "Sub tasks not equals");

        var subTaskSet = taskManager.getPrioritizedTasks();
        Assertions.assertTrue(subTaskSet.contains(subTask), "Given subTask not found in the set");

        Assertions.assertEquals(time, saveSubTask.getStartTime(), "Start time is incorrect");
        Assertions.assertEquals(60, saveSubTask.getDuration(), "Duration is incorrect");

        var subTasks = taskManager.getListOfSubTasks();
        Assertions.assertNotNull(subTasks, "The list is null");
        Assertions.assertEquals(1, subTasks.size(), "Incorrect number of tasks");
        Assertions.assertEquals(subTask, subTasks.get(0), "Incorrect epic in list");

        var subTaskFromEpic = epic.getSubTasks().get(0);
        Assertions.assertNotNull(subTaskFromEpic, "Sub task from epic is null");
        Assertions.assertEquals(subTask, subTaskFromEpic, "Sub task from epic and original is not equals");
    }


    @Test
    void getListOfTasksTest() {
        var task = new Task("test", "test", time, 60);
        var task1 = new Task("test", "test", time.plusHours(1), 60);
        var task2 = new Task("test", "test", time.plusHours(2), 60);

        taskManager.addTask(task);
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        var test = taskManager.getListOfTasks();
        Assertions.assertNotNull(test, "The list is null");
        Assertions.assertEquals(test.size(), 3, "Incorrect number of tasks");
    }

    @Test
    void getListOfSubTasksTest() {
        var epic = new Epic("test", "test");
        var task = new SubTask("test", "test", 1, time, 60);
        var task1 = new SubTask("test", "test", 1, time.plusHours(1), 60);
        var task2 = new SubTask("test", "test", 1, time.plusHours(2), 60);


        taskManager.addEpic(epic);
        taskManager.addSubTask(task);
        taskManager.addSubTask(task1);
        taskManager.addSubTask(task2);

        var test = taskManager.getListOfSubTasks();
        Assertions.assertNotNull(test, "The list is null");
        Assertions.assertEquals(test.size(), 3, "Incorrect number of subtasks");
    }

    @Test
    void getListOfEpicsTest() {
        var epic = new Epic("test", "test");
        var epic1 = new Epic("test", "test");
        var epic2 = new Epic("test", "test");

        taskManager.addEpic(epic);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        var test = taskManager.getListOfEpics();
        Assertions.assertNotNull(test, "The list is null");
        Assertions.assertEquals(test.size(), 3, "Incorrect number of epics");
    }

    @Test
    void getPrioritizedTasksTest() {
        var epic = new Epic("test", "test");

        var subTask1 = new SubTask("late task", "test", 1, time.plusHours(2), 60);
        var subTask2 = new SubTask("early task", "test", 1, time.minusHours(2), 60);

        var task1 = new Task("test", "test", time.plusHours(25), 60);
        var task2 = new Task("test", "test", time.plusHours(30), 60);

        taskManager.addEpic(epic);

        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        var prioritizedTasks = taskManager.getPrioritizedTasks();
        Assertions.assertEquals(4, prioritizedTasks.size(), "Incorrect number of tasks");

        Assertions.assertEquals(subTask2, prioritizedTasks.first(), "The first task is not the one with the earliest start time");
    }


    @Test
    void getListOfSubTasksOfEpicTest() {
        var epic = new Epic("test", "test");
        var task = new SubTask("test", "test", 1, time, 60);
        var task1 = new SubTask("test", "test", 1, time.plusHours(1), 60);
        var task2 = new SubTask("test", "test", 1, time.plusHours(2), 60);
        taskManager.addEpic(epic);

        var test = taskManager.getListOfSubTasksOfEpic(epic);
        Assertions.assertNotNull(test, "the list is null");
        Assertions.assertTrue(test.isEmpty());

        taskManager.addSubTask(task);
        taskManager.addSubTask(task1);
        taskManager.addSubTask(task2);

        test = taskManager.getListOfSubTasksOfEpic(epic);
        Assertions.assertNotNull(test, "the list is null");
        Assertions.assertEquals(3, test.size());
    }

    @Test
    void deleteListOfTasksTest() {
        var task = new Task("test", "test", time, 60);
        var task1 = new Task("test", "test", time.plusHours(1), 60);
        var task2 = new Task("test", "test", time.plusHours(2), 60);

        taskManager.deleteTasks();

        Assertions.assertNotNull(taskManager.getListOfTasks(), "the list is null");
        Assertions.assertEquals(taskManager.getListOfTasks().size(), 0, "the size is not zero");

        taskManager.addTask(task);
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        taskManager.deleteTasks();
        Assertions.assertNotNull(taskManager.getListOfTasks(), "the list is null");
        Assertions.assertEquals(taskManager.getListOfTasks().size(), 0, "the size is not zero");
        Assertions.assertNotNull(taskManager.getPrioritizedTasks(), "The tree set is null");
        Assertions.assertEquals(taskManager.getPrioritizedTasks().size(), 0, "The tree set is not zero");
    }

    @Test
    void deleteListOfEpicTest() {
        var epic = new Epic("test", "test");
        var epic1 = new Epic("test", "test");
        var epic2 = new Epic("test", "test");
        var subTask = new SubTask("test", "test", 1, time, 60);

        taskManager.deleteEpicTasks();
        Assertions.assertNotNull(taskManager.getListOfEpics(), "the list is null");
        Assertions.assertEquals(taskManager.getListOfEpics().size(), 0, "the size is not zero");

        taskManager.addEpic(epic);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        taskManager.addSubTask(subTask);


        taskManager.deleteEpicTasks();
        Assertions.assertNotNull(taskManager.getListOfEpics(), "the list is null");
        Assertions.assertEquals(taskManager.getListOfEpics().size(), 0, "the size is not zero");

        Assertions.assertNotNull(taskManager.getListOfSubTasks(), "the list is null");
        Assertions.assertEquals(taskManager.getListOfSubTasks().size(), 0, "the size is not zero");
        Assertions.assertNotNull(taskManager.getPrioritizedTasks(), "The tree set is null");
        Assertions.assertEquals(taskManager.getPrioritizedTasks().size(), 0, "The tree set is not zero");
    }

    @Test
    void deleteListOfSubTasksTest() {
        var epic = new Epic("test", "test");
        var subTask = new SubTask("test", "test", 1, time, 60);
        var subTask1 = new SubTask("test", "test", 1, time.plusHours(1), 60);
        var subTask2 = new SubTask("test", "test", 1, time.plusHours(2), 60);

        taskManager.deleteSubTasks();

        Assertions.assertNotNull(taskManager.getListOfSubTasks(), "the list is null");
        Assertions.assertEquals(taskManager.getListOfSubTasks().size(), 0, "the size is not zero");

        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);

        taskManager.deleteSubTasks();

        Assertions.assertNotNull(taskManager.getListOfSubTasks(), "the list is null");
        Assertions.assertEquals(taskManager.getListOfSubTasks().size(), 0, "the size is not zero");
        Assertions.assertEquals(0, epic.getSubTasks().size(), "List is not empty");
        Assertions.assertNotNull(taskManager.getPrioritizedTasks(), "The tree set is null");
        Assertions.assertEquals(taskManager.getPrioritizedTasks().size(), 0, "The tree set is not zero");
    }

    @Test
    void getTaskByIdTest() {
        var task = new Task("test", "test", time, 60);
        taskManager.addTask(task);

        var retrievedTask = taskManager.getTaskById(1);

        Assertions.assertEquals(task, retrievedTask, "Retrieved task is not the same as the original task");
        Assertions.assertTrue(taskManager.getHistoryManager().getHistory().contains(task), "Task is not added to the history manager");
    }

    @Test
    void getSubtaskByIdTest() {
        var epic = new Epic("test", "test");
        var subTask = new SubTask("test", "test", 1, time, 60);
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask);

        var retrievedSubTask = taskManager.getSubtaskById(2);

        Assertions.assertEquals(subTask, retrievedSubTask, "Retrieved subtask is not the same as the original subtask");
        Assertions.assertTrue(taskManager.getHistoryManager().getHistory().contains(subTask), "Subtask is not added to the history manager");
    }

    @Test
    void getEpicByIdTest() {
        var epic = new Epic("test", "test");
        taskManager.addEpic(epic);

        var retrievedEpic = taskManager.getEpicById(1);

        Assertions.assertEquals(epic, retrievedEpic, "Retrieved epic is not the same as the original epic");
        Assertions.assertTrue(taskManager.getHistoryManager().getHistory().contains(epic), "Epic is not added to the history manager");
    }

    @Test
    void deleteTaskByIdTest() {
        var task = new Task("test", "test", time, 60);
        taskManager.addTask(task);
        taskManager.deleteTaskById(1);
        Assertions.assertThrows(IllegalArgumentException.class, () -> taskManager.getTaskById(1));
        Assertions.assertFalse(taskManager.getPrioritizedTasks().contains(task));
        Assertions.assertFalse(taskManager.getListOfTasks().contains(task));
    }

    @Test
    void deleteSubtaskByIdTest() {
        var epic = new Epic("test", "test");
        var subTask = new SubTask("test", "test", 1, time, 60);
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask);
        taskManager.deleteSubTaskById(2);
        Assertions.assertThrows(IllegalArgumentException.class, () -> taskManager.getSubtaskById(2));
        Assertions.assertEquals(0, epic.getSubTasks().size(), "Sub task doesnt delete in the epic");
        Assertions.assertFalse(taskManager.getPrioritizedTasks().contains(subTask));
        Assertions.assertFalse(taskManager.getListOfSubTasks().contains(subTask));
    }

    @Test
    void deleteEpicByIdTest() {
        var epic = new Epic("test", "test");
        var epic1 = new Epic("test", "test");
        var subTask = new SubTask("test1", "test", 1, time.plusHours(5), 60); //2
        var subTaskTest = new SubTask("test2", "test", 2, time.minusHours(3), 60); //1
        var subTask1 = new SubTask("test3", "test", 1, time.plusHours(2), 60); //1

        taskManager.addEpic(epic);
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subTask);
        taskManager.addSubTask(subTaskTest);
        taskManager.addSubTask(subTask1);
        taskManager.deleteEpicById(1);

        Assertions.assertThrows(IllegalArgumentException.class, () -> taskManager.getEpicById(1));

        var test = taskManager.getListOfSubTasks();
        Assertions.assertEquals(1, test.size(), "subtasks not deleted");
        Assertions.assertEquals(test.get(0), subTaskTest);
    }

    @Test
    void changeTaskTest() {
        var task = new Task("test", "test", time, 60);
        taskManager.addTask(task);
        var taskToChange = new Task("new test", "new test", Status.IN_PROGRESS, 1, time.plusHours(1), 60);
        taskManager.changeTask(taskToChange);
        var test = taskManager.getTaskById(1);
        Assertions.assertEquals(taskToChange, test);
        Assertions.assertTrue(taskManager.getPrioritizedTasks().contains(taskToChange));
    }


    @Test
    void changeSubTaskTest() {
        var epic = new Epic("test", "test");
        var subTask = new SubTask("test", "test", 1, time, 60);
        var subTask1 = new SubTask("test", "test", 1, time.plusHours(2), 60);
        var subTaskToChange = new SubTask("test3", "test", Status.NEW, 2, time.plusHours(3), 60, 1);
        List<SubTask> subTasksTest = new ArrayList<>();

        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask);
        taskManager.addSubTask(subTask1);

        taskManager.changeSubTask(subTaskToChange);

        subTasksTest.add(subTask1);
        subTasksTest.add(subTaskToChange);
        Assertions.assertEquals(epic.getSubTasks(), subTasksTest);
        Assertions.assertTrue(taskManager.getPrioritizedTasks().contains(subTaskToChange));
    }

}

