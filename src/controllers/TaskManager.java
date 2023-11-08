package controllers;

import exceptions.ManagerSaveException;
import exceptions.TimeCrossingException;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.TreeSet;

public interface TaskManager {
    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubTask(SubTask subTask) throws TimeCrossingException;

    ArrayList<Task> getListOfTasks();

    ArrayList<SubTask> getListOfSubTasks();

    ArrayList<Epic> getListOfEpics();

    ArrayList<SubTask> getListOfSubTasksOfEpic(Epic epic);

    TreeSet<Task> getPrioritizedTasks();

    void deleteTasks();

    void deleteSubTasks();

    void deleteEpicTasks();

    Task getTaskById(int id);

    SubTask getSubtaskById(int id);

    Epic getEpicById(int id);

    void deleteTaskById(int id);

    void deleteSubTaskById(int id);

    void deleteEpicById(int id);

    void changeTask(Task task);

    void changeEpic(Epic epic);

    void changeSubTask(SubTask subTask);

    void changeSubTaskStatus(SubTask subTask, Status status);


    HistoryManager getHistoryManager();

}
