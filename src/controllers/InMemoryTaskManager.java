package controllers;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager{
    private int id = 0;
    private final HashMap<Integer, Task> taskMap = new HashMap<>();
    private final HashMap<Integer, Epic> epicMap = new HashMap<>();
    private final HashMap<Integer, SubTask> subTaskMap = new HashMap<>();
    public HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public Task addTask(Task task) {
        int i = incriminateId();
        task.setId(i);
        taskMap.put(i, task);
        return task;
    }

    @Override
    public Epic addEpic(Epic epic) {
        int i = incriminateId();
        epic.setId(i);
        epicMap.put(i, epic);
        return epic;
    }

    @Override
    public SubTask addSubTask(SubTask subTask) {
        int i = incriminateId();
        subTask.setId(i);
        subTaskMap.put(i, subTask);
        epicMap.get(subTask.getEpicId()).setSubTasks(subTask);
        return subTask;
    }

    @Override
    public ArrayList<Task> getListOfTasks() {
        return new ArrayList<>(taskMap.values());
    }

    @Override
    public ArrayList<SubTask> getListOfSubTasks() {
        return new ArrayList<>(subTaskMap.values());
    }

    @Override
    public ArrayList<Epic> getListOfEpics() {
        return new ArrayList<>(epicMap.values());
    }

    @Override
    public ArrayList<SubTask> getListOfSubTasksOfEpic(Epic epic) {
        return epic.getSubTasks();
    }

    @Override
    public void deleteTasks() {
        taskMap.clear();
    }

    @Override
    public void deleteSubTasks() {
        subTaskMap.clear();
        for (Epic epic : epicMap.values()) {
            epic.deleteSubTask();
        }
    }

    @Override
    public void deleteEpicTasks() {
        epicMap.clear();
        subTaskMap.clear();
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(taskMap.get(id));
        return taskMap.get(id);
    }

    @Override
    public SubTask getSubtaskById(int id) {
        historyManager.add(subTaskMap.get(id));
        return subTaskMap.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epicMap.get(id));
        return epicMap.get(id);
    }

    @Override
    public void deleteTaskById(int id) {
        taskMap.remove(id);
    }

    @Override
    public void deleteSubTaskById(int id) {
        subTaskMap.remove(id);
        epicMap.get(subTaskMap.get(id).getEpicId()).removeSubTask(id);
    }

    @Override
    public void deleteEpicById(int id) {
        for (SubTask subTask : epicMap.get(id).getSubTasks()) {
            subTaskMap.remove(subTask.getId());
        }
        epicMap.remove(id);
    }

    @Override
    public void changeTask(Task task) {
        taskMap.put(task.getId(), task);
    }

    @Override
    public void changeEpic(Epic epic) {
        epicMap.put(epic.getId(), epic);
    }

    @Override
    public void chageSubTask(SubTask subTask) {
        subTaskMap.put(subTask.getId(), subTask);
        epicMap.get(subTask.getEpicId()).removeSubTask(subTask.getId());
        epicMap.get(subTask.getEpicId()).setSubTasks(subTask);
    }

    private void changeEpicStatus(Epic epic) {
        int new_count = 0;
        int done_count = 0;
        for (SubTask subTask : epic.getSubTasks()) {
            if (subTask.getStatus() == Status.NEW) {
                new_count++;
            } else if (subTask.getStatus() == Status.DONE) {
                done_count++;
            }
        }
        if (new_count == epic.getSubTasks().size()) {
            epic.setStatus(Status.NEW);
        } else if (done_count == epic.getSubTasks().size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public void changeSubTaskStatus(SubTask subTask, Status status) {
        subTask.setStatus(status);
        changeEpicStatus(epicMap.get(subTask.getEpicId()));
    }

    @Override
    public void changeTaskStatus(Task task, Status status) {
        task.setStatus(status);
    }

    private int incriminateId() {
        return ++id;
    }

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }


}
