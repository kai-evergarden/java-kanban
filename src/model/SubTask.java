package model;

import java.time.LocalDateTime;
import java.util.Objects;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, String description, int epicId, LocalDateTime startTime, int duration) {
        super(name, description, TaskType.SUBTASK, startTime, duration);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, Status status, int id, LocalDateTime startTime, int duration, int epicId) {
        super(name, description, status, id, startTime, duration);
        this.epicId = epicId;
    }

    public SubTask(int id, TaskType taskType, String name, Status status, String description, LocalDateTime startTime, int duration, int epicId) {
        super(id, taskType, name, status, description, startTime, duration);
        this.epicId = epicId;
    }
    public int getEpicId() {
        return epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return epicId == subTask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }
}
