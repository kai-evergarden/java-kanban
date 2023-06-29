import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int id = 0;
    private final HashMap<Integer, Task> taskMap = new HashMap<>();
    private final HashMap<Integer, Epic> epicMap = new HashMap<>();
    private final HashMap<Integer, SubTask> subTaskMap = new HashMap<>();

    public void addTask(String name, String description, String status) {
        Task task = new Task(name, description, status);
        putTask(task);
    }

    public Task putTask(Task task) {
        int i = incriminateId();
        task.setId(i);
        taskMap.put(i, task);
        return task;
    }

    public void addEpic(String name, String description, String status) {
        Epic epic = new Epic(name, description, status);
        putEpic(epic);
    }

    public Epic putEpic(Epic epic) {
        int i = incriminateId();
        epic.setId(i);
        epicMap.put(i, epic);
        return epic;
    }

    public void addSubTask(int epicId, String name, String description, String status) {
        SubTask subTask = new SubTask(name, description, status, epicId);
        putSubTask(subTask);
    }

    public SubTask putSubTask(SubTask subTask) {
        int i = incriminateId();
        subTask.setId(i);
        subTaskMap.put(i, subTask);
        epicMap.get(subTask.getEpicId()).setSubTasks(subTask);
        changeEpicStatus(epicMap.get(subTask.getEpicId()));
        return subTask;
    }

    public ArrayList<Task> getListOfTasks() {
        return new ArrayList<>(taskMap.values());
    }

    public ArrayList<SubTask> getListOfSubTasks() {
        return new ArrayList<>(subTaskMap.values());
    }

    public ArrayList<Epic> getListOfEpics() {
        return new ArrayList<>(epicMap.values());
    }

    public void deleteTasks() {
        taskMap.clear();
    }

    public void deleteSubTasks() {
        subTaskMap.clear();
        for(Epic epic : epicMap.values()) {
            epic.deleteSubTask();
        }
    }

    public void deleteEpicTasks() {
        epicMap.clear();
    }

    public Task getTaskById(int id) {
        return taskMap.get(id);
    }

    public SubTask getSubtaskById(int id) {
        return subTaskMap.get(id);
    }

    public Epic getEpicById(int id) {
        return epicMap.get(id);
    }

    public void deleteTaskById(int id) {
        taskMap.remove(id);
    }

    public void deleteSubTaskById(int id) {
        subTaskMap.remove(id);
        epicMap.get(subTaskMap.get(id).getEpicId()).removeSubTask(id);
    }

    public void deleteEpic(int id) {
        epicMap.remove(id);
    }

    public void changeTask(Task task) {
        taskMap.put(task.getId(), task);
    }

    public void changeEpic(Epic epic) {
        epicMap.put(epic.getId(), epic);
    }

    public void chageSubTask(SubTask subTask) {
        subTaskMap.put(subTask.getId(), subTask);
        epicMap.get(subTask.getEpicId()).removeSubTask(subTask.getId());
        epicMap.get(subTask.getEpicId()).setSubTasks(subTask);
    }

    public void changeEpicStatus(Epic epic) {
        int new_count = 0;
        int done_count = 0;
        for(SubTask subTask : epic.getSubTasks()) {
            if(subTask.getStatus().equals("NEW")) {
                new_count++;
            } else if(subTask.getStatus().equals("DONE")) {
                done_count++;
            }
        }
        if(new_count == epic.getSubTasks().size())  {
            epic.setStatus("NEW");
        } else if(done_count == epic.getSubTasks().size()) {
            epic.setStatus("DONE");
        } else {
            epic.setStatus("IN_PROGRESS");
        }
    }


    public int incriminateId() {
        return ++id;
    }
}
