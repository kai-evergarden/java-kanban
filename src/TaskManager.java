import java.util.HashMap;

public class TaskManager {
    private int id = 0;
    HashMap<Integer, Task> taskMap = new HashMap<>();
    HashMap<Integer, Epic> epicMap = new HashMap<>();


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

    public void addSubTask(int id, String name, String description, String status) {
        SubTask subTask = new SubTask(name, description, status, id);
        putSubTask(subTask);
    }

    public SubTask putSubTask(SubTask subTask) {
        int i = incriminateId();
        subTask.setId(i);
        epicMap.get(subTask.getEpicId()).setSubTasks(subTask);
        epicMap.get(subTask.getEpicId()).printTask();
        return subTask;
    }

    public int incriminateId() {
        return ++id;
    }
}
