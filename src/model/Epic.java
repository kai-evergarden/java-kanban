package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<SubTask> subTasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, TaskType.EPIC);
    }

    public Epic(String name, String description, int id) {
        super(name, description, id);
    }

    public Epic(int id, TaskType taskType, String name, Status status, String description, LocalDateTime startTime, Integer duration) {
        super(id, taskType, name, status, description, startTime, duration);
    }

    public void setSubTasks(SubTask subTask) {
        subTasks.add(subTask);
        subTasks.sort(Comparator.comparing(Task::getStartTime));
        setStartTime();
        setDuration();
    }

    public void setSubTask(List<SubTask> subTasks) {
        this.subTasks = (ArrayList<SubTask>) subTasks;
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void removeSubTask(int id) {
        subTasks.removeIf(subTask -> subTask.getId() == id);
        subTasks.sort(Comparator.comparing(Task::getStartTime));
    }

    public void deleteSubTask() {
        subTasks.clear();
    }

    @Override
    public int getDuration() {
        int duration = 0;
        for (SubTask o : subTasks)
            duration += o.getDuration();
        return duration;
    }

    @Override
    public LocalDateTime getEndTime() {
        return subTasks.get(subTasks.size() - 1).getEndTime();
    }

    @Override
    public LocalDateTime getStartTime() {
        if (!subTasks.isEmpty())
            return subTasks.get(0).getStartTime();
        return null;
    }

    public void setStartTime() {
        if (!subTasks.isEmpty())
            super.startTime = getStartTime();
        else
            super.startTime = null;
    }

    public void setDuration() {
        super.duration = getDuration();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTasks, epic.subTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTasks);
    }
}
