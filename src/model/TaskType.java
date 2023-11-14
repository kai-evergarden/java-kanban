package model;

public enum TaskType {
    TASK,
    SUBTASK,
    EPIC;

    public String getName() {
        return this.name().toLowerCase();
    }
}
