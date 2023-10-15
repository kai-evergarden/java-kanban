import controllers.Managers;
import controllers.TaskManager;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        Task task1 = new Task("Build a house", "build a house");
        Task task2 = new Task("Create app", "create app");
        Epic epic1 = new Epic("epic build a house", "aaaa");
        SubTask subTask1Epic1 = new SubTask("1 sub for epic 1", "aaa", 3);
        SubTask subTask2Epic1 = new SubTask("2 sub for epic 1", "a", 3);
        Epic epic2 = new Epic("epic create app", "aaa");
        SubTask subTask1Epic2 = new SubTask("1 sub for epic 2", "aaa", 6);
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subTask1Epic1);
        taskManager.addSubTask(subTask2Epic1);
        taskManager.addEpic(epic2);
        taskManager.addSubTask(subTask1Epic2);
        System.out.println("\nПроверка создания объектов: \n");
        printTest(taskManager);
        taskManager.changeSubTaskStatus(subTask1Epic1, Status.DONE);
        taskManager.changeSubTaskStatus(subTask2Epic1, Status.IN_PROGRESS);
        taskManager.changeSubTaskStatus(subTask1Epic2, Status.DONE);
        System.out.println("Провнерка получения саб тасков определенного эпика: \n");
        printTest(taskManager);
        System.out.println("-------------------------------");
        System.out.println(taskManager.getListOfSubTasksOfEpic(epic2));
        System.out.println("-------------------------------\n");
        System.out.println("Проверка истори: \n");
        taskManager.getTaskById(1);
        System.out.println(taskManager.getHistoryManager().getHistory());
        taskManager.getTaskById(1);
        taskManager.getEpicById(3);
        System.out.println(taskManager.getHistoryManager().getHistory());
        taskManager.getTaskById(1);
        System.out.println(taskManager.getHistoryManager().getHistory());
        taskManager.getTaskById(1);
        System.out.println(taskManager.getHistoryManager().getHistory());
        taskManager.getTaskById(1);
        System.out.println(taskManager.getHistoryManager().getHistory());
        taskManager.getTaskById(1);
        System.out.println(taskManager.getHistoryManager().getHistory());
        taskManager.getTaskById(1);
        System.out.println(taskManager.getHistoryManager().getHistory());
        taskManager.getTaskById(1);
        System.out.println(taskManager.getHistoryManager().getHistory());
        taskManager.getTaskById(1);
        System.out.println(taskManager.getHistoryManager().getHistory());
        taskManager.getTaskById(1);
        System.out.println(taskManager.getHistoryManager().getHistory().size());
    }

    public static void printTest(TaskManager taskManager) {
        System.out.println("-------------------------------");
        System.out.println(taskManager.getListOfTasks());
        System.out.println(taskManager.getListOfEpics());
        System.out.println(taskManager.getListOfSubTasks());
        System.out.println("-------------------------------\n");
    }
}
