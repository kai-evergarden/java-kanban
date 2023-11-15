package controllers.managers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import controllers.managers.FileBackedTasksManager;
import controllers.KV.KVTaskClient;
import controllers.managers.Managers;
import model.Epic;
import model.SubTask;
import model.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient client;
    private final Gson gson;

    public HttpTaskManager(String uri) throws IOException {
        this.client = new KVTaskClient(uri);
        this.gson = Managers.getGson();
        loadFromServer();
    }

    public void loadFromServer() {
        String tasks = client.load("task");
        if (tasks != null) {
            Type tasksListType = new TypeToken<ArrayList<Task>>() {
            }.getType();
            List<Task> taskList = gson.fromJson(tasks, tasksListType);
            System.out.println(tasks);
            System.out.println(taskList);
            if (taskList != null)
                taskList.forEach(task -> taskMap.put(task.getId(), task));
        }
        String subtasks = client.load("subtask");
        if (subtasks != null) {
            Type subtasksListType = new TypeToken<ArrayList<SubTask>>() {
            }.getType();
            List<SubTask> subTaskList = gson.fromJson(tasks, subtasksListType);
            if (subTaskList != null)
                subTaskList.forEach(task -> subTaskMap.put(task.getId(), task));
        }
        String epics = client.load("epics");
        if (epics != null) {
            Type epicsListType = new TypeToken<ArrayList<Epic>>() {
            }.getType();
            List<Epic> epicList = gson.fromJson(epics, epicsListType);
            if (epicList != null)
                epicList.forEach(task -> epicMap.put(task.getId(), task));
        }
        String history = client.load("history");
        if (history != null) {
            Type historyListType = new TypeToken<ArrayList<Integer>>() {
            }.getType();
            List<Integer> historyList = gson.fromJson(history, historyListType);
            if (historyList != null)
                super.fillHistory(historyList);
        }
        String priority = client.load("priority");
        if (priority != null) {
            Type priorityListType = new TypeToken<ArrayList<Task>>() {
            }.getType();
            List<Task> priorityList = gson.fromJson(priority, priorityListType);
            if (priorityList != null)
                taskTreeSet.addAll(priorityList);
        }
    }

    @Override
    public void save() {
        String task = gson.toJson(getListOfTasks());
        client.put("task", task);
        String epic = gson.toJson(getListOfEpics());
        client.put("epic", epic);
        String subtasks = gson.toJson(getListOfSubTasks());
        client.put("subtask", subtasks);
        String history = getHistoryManager().getHistory()
                .stream()
                .map(Task::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        client.put("history", history);
        String priorityTask = getPrioritizedTasks()
                .stream()
                .map(Task::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        client.put("priority", priorityTask);

    }
}