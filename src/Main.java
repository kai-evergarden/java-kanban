import controllers.*;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.io.File;
import java.io.IOException;


public class Main {
    public static void main(String[] args) {
//        try {
//            var taskManager = FileBackedTasksManager.loadFromFile(FILE);
//            Task task1 = new Task("FirstTask", "test for first task");
//            Task task2 = new Task("SecondTask", "test for second task");
//            Task task3 = new Task("ThirdTask", "test for third task");
//            Epic epic1 = new Epic("EPIC", "bleba");
//            SubTask subTask = new SubTask("SubTask for epic with id 4", "mega subtask", 4);
//            SubTask subTask1 = new SubTask("SubTask 1 for epic with id 4", "mega subtask", 4);
//            taskManager.addTask(task1);
//            taskManager.addTask(task2);
//            taskManager.addTask(task3);
//            taskManager.addEpic(epic1);
//            taskManager.addSubTask(subTask);
//            taskManager.addSubTask(subTask1);
//            Task task = new Task("BlebaTask", "test for new version of task");
//            SubTask subTask = new SubTask("SubTask 2 for epic with id 4", "mega subtask", 4);
//            taskManager.addTask(task) ;
//            taskManager.addSubTask(subTask);
//            taskManager.getEpicById(4);
//            taskManager.getTaskById(1);
//            taskManager.getTaskById(2);
//        } catch (ManagerSaveException e) {
//            System.out.println("bleba");
        }
//        Task task1 = new Task("Buba", "build a house");
//        taskManager.addTask(task1);
//    }

    public static void printTest(TaskManager taskManager) {
        System.out.println("-------------------------------");
        System.out.println(taskManager.getListOfTasks());
        System.out.println(taskManager.getListOfEpics());
        System.out.println(taskManager.getListOfSubTasks());
        System.out.println("-------------------------------\n");
    }
}
