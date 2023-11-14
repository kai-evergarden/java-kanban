package managers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import controllers.FileBackedTasksManager;
import controllers.KVTaskClient;
import controllers.Managers;
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
        super();
        this.client = new KVTaskClient(uri);
        this.gson = Managers.getGson();
    }

    public void saveFromServer() {
        try {
            String tasks = client.load("task");
            if (tasks != null) {
                Type tasksListType = new TypeToken<ArrayList<Task>>() {
                }.getType();
                List<Task> taskList = gson.fromJson(tasks, tasksListType);
                taskList.forEach(task -> taskMap.put(task.getId(), task));
            }
            String subtasks = client.load("subtask");
            if (subtasks != null) {
                Type subtasksListType = new TypeToken<ArrayList<SubTask>>() {
                }.getType();
                List<SubTask> subTaskList = gson.fromJson(tasks, subtasksListType);
                subTaskList.forEach(task -> subTaskMap.put(task.getId(), task));
            }
            String epics = client.load("epics");
            if (epics != null) {
                Type epicsListType = new TypeToken<ArrayList<Epic>>() {
                }.getType();
                List<Epic> epicList = gson.fromJson(epics, epicsListType);
                epicList.forEach(task -> epicMap.put(task.getId(), task));
            }
            String history = client.load("history");
            if (history != null) {
                Type historyListType = new TypeToken<ArrayList<Integer>>() {
                }.getType();
                List<Integer> historyList = gson.fromJson(history, historyListType);
                super.fillHistory(historyList);
            }
            String priority = client.load("priority");
            if (priority != null) {
                Type priorityListType = new TypeToken<ArrayList<Task>>() {
                }.getType();
                List<Task> priorityList = gson.fromJson(priority, priorityListType);
                taskTreeSet.addAll(priorityList);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save() {
        try {
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
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}