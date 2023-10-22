package model;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, String description, int epicId) {
        super(name, description, TaskType.SUBTASK);
        this.epicId = epicId;
    }

    public SubTask(int id, TaskType taskType, String name, Status status, String description, int epicId) {
        super(id, taskType, name, status, description);
        this.epicId = epicId;
    }
    public int getEpicId() {
        return epicId;
    }


}
