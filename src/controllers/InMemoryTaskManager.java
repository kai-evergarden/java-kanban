package controllers;

import exceptions.TimeCrossingException;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int id = 0;
    protected final HashMap<Integer, Task> taskMap = new HashMap<>();
    protected final HashMap<Integer, Epic> epicMap = new HashMap<>();
    protected final HashMap<Integer, SubTask> subTaskMap = new HashMap<>();
    protected final TreeSet<Task> taskTreeSet = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    protected final HistoryManager historyManager = Managers.getDefaultHistory();


    @Override
    public void addTask(Task task) {
        if (task == null) throw new IllegalArgumentException("Task cannot be null");
        if (isIntersection(task)) throw new TimeCrossingException("Intersection found");
        int i = incriminateId();
        task.setId(i);
        taskMap.put(i, task);
        taskTreeSet.add(task);
    }

    @Override
    public void addEpic(Epic epic) {
        if (epic == null) throw new IllegalArgumentException("Epic cannot be null");
        int i = incriminateId();
        epic.setId(i);
        epicMap.put(i, epic);
    }


    @Override
    public void addSubTask(SubTask subTask) {
        if (subTask == null) throw new IllegalArgumentException("SubTask cannot be null");
        if (isIntersection(subTask)) throw new TimeCrossingException("Intersection found");
        int i = incriminateId();
        subTask.setId(i);
        subTaskMap.put(i, subTask);
        var epic = epicMap.get(subTask.getEpicId());
        epic.setSubTasks(subTask);
        epic.setStartTime();
        epic.setDuration();
        taskTreeSet.add(subTask);
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
    public TreeSet<Task> getPrioritizedTasks() {
        return taskTreeSet;
    }

    @Override
    public ArrayList<SubTask> getListOfSubTasksOfEpic(Epic epic) {
        return epic.getSubTasks();
    }

    @Override
    public void deleteTasks() {
        taskMap.values().stream().filter(taskTreeSet::contains).forEach(taskTreeSet::remove);
        taskMap.clear();
    }

    @Override
    public void deleteSubTasks() {
        subTaskMap.values().stream().filter(taskTreeSet::contains).forEach(taskTreeSet::remove);
        subTaskMap.clear();
        epicMap.values().forEach(epic -> {
            epic.deleteSubTask();
            epic.setStatus(Status.NEW);
        });
    }

    @Override
    public void deleteEpicTasks() {
        epicMap.values().stream().filter(taskTreeSet::contains).forEach(taskTreeSet::remove);
        //стрим для фильтрация использую
        epicMap.clear();
        deleteSubTasks();
    }

    @Override
    public Task getTaskById(int id) {
        if (!taskMap.containsKey(id)) throw new IllegalArgumentException("Id out of bounds");
        historyManager.add(taskMap.get(id));
        return taskMap.get(id);
    }

    @Override
    public SubTask getSubtaskById(int id) {
        if (!subTaskMap.containsKey(id)) throw new IllegalArgumentException("Id out of bounds");
        historyManager.add(subTaskMap.get(id));
        return subTaskMap.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        if (!epicMap.containsKey(id)) throw new IllegalArgumentException("Id out of bounds");
        historyManager.add(epicMap.get(id));
        return epicMap.get(id);
    }

    @Override
    public void deleteTaskById(int id) {
        if (!taskMap.containsKey(id)) throw new IllegalArgumentException("Id out of bounds");
        taskTreeSet.remove(taskMap.get(id));
        taskMap.remove(id);
    }

    @Override
    public void deleteSubTaskById(int id) {
        if (!subTaskMap.containsKey(id)) throw new IllegalArgumentException("Id out of bounds");
        taskTreeSet.remove(subTaskMap.get(id));
        var epic = epicMap.get(subTaskMap.get(id).getEpicId());
        epic.removeSubTask(id);
        epic.setStartTime();
        epic.setDuration();
        changeEpicStatus(epic);
        subTaskMap.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        if (!epicMap.containsKey(id)) throw new IllegalArgumentException("Id out of bounds");
        taskTreeSet.remove(epicMap.get(id));
        epicMap.get(id).getSubTasks().forEach(subTask -> {
            subTaskMap.remove(subTask.getId());
            taskTreeSet.remove(subTask);
        });
        epicMap.remove(id);
    }

    @Override
    public void changeTask(Task task) {
        if (task == null) throw new IllegalArgumentException("Task cannot be null");
        if (isIntersection(task)) throw new TimeCrossingException("Intersection found");
        taskTreeSet.removeIf(taskFromTreeSet -> taskFromTreeSet.getId() == task.getId());
        taskTreeSet.add(task);
        taskMap.put(task.getId(), task);
    }

    @Override
    public void changeEpic(Epic epic) {
        if (epic == null) throw new IllegalArgumentException("Epic cannot be null");
        if (isIntersection(epic)) throw new TimeCrossingException("Intersection found");
        List<SubTask> subTaskList = epicMap.get(epic.getId()).getSubTasks();
        epic.setSubTask(subTaskList);
        epicMap.put(epic.getId(), epic);
    }

    @Override
    public void changeSubTask(SubTask subTask) {
        if (subTask == null) throw new IllegalArgumentException("SubTask cannot be null");
        if (isIntersection(subTask)) throw new TimeCrossingException("Intersection found");
        taskTreeSet.removeIf(taskFromTreeSet -> taskFromTreeSet.getId() == subTask.getId());
        subTaskMap.put(subTask.getId(), subTask);
        var epic = epicMap.get(subTask.getEpicId());
        epic.removeSubTask(subTask.getId());
        epic.setSubTasks(subTask);
        epic.setStartTime();
        epic.setDuration();
        changeEpicStatus(epic);
        taskTreeSet.add(subTask);
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

    private boolean isIntersection(Task task) {
        return taskTreeSet.stream()
                .anyMatch(existingTask -> existingTask.intersects(task));
    }

    @Override
    public void changeSubTaskStatus(SubTask subTask, Status status) { //
        subTask.setStatus(status);
        changeEpicStatus(epicMap.get(subTask.getEpicId()));
    }


    private int incriminateId() {
        return ++id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

}
