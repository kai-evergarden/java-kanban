package model;

import com.ibm.java.diagnostics.utils.plugins.LocalPriorityClassloader;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected TaskType taskType;
    protected int id;
    protected LocalDateTime startTime = null;
    protected int duration = 0;
    protected Status status;

    public Task(String name, String description, LocalDateTime startTime, int duration) {
        this.name = name;
        this.description = description;
        this.taskType = TaskType.TASK;
        this.status = Status.NEW;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String name, String description, TaskType taskType, LocalDateTime startTime, int duration) {
        this.name = name;
        this.description = description;
        this.taskType = taskType;
        this.status = Status.NEW;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String name, String description, TaskType taskType) {
        this.name = name;
        this.description = description;
        this.taskType = taskType;
        this.status = Status.NEW;
    }

    public Task(String name, String description, Status status, int id, LocalDateTime startTime, int duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(int id, TaskType taskType, String name, String description, LocalDateTime startTime, int duration) {
        this.id = id;
        this.taskType = taskType;
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String name, String description, int id) {
        this.name = name;
        this.description = description;
        this.id = id;
    }

    public Task(int id, TaskType taskType, String name, Status status, String description, LocalDateTime startTime, int duration) {
        this.id = id;
        this.taskType = taskType;
        this.name = name;
        this.status = status;
        this.description = description;
        this.startTime = startTime;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "model.Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }


    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null)
            return startTime.plusMinutes(duration);
        return LocalDateTime.MIN;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description) && taskType == task.taskType && status == task.status;
    }

    public boolean intersects(Task otherTask) {
        return (this.startTime.isAfter(otherTask.getStartTime()) ||
                this.startTime.isEqual(otherTask.getStartTime())) &&
                this.startTime.isBefore(otherTask.getEndTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, taskType, id, status);
    }
}