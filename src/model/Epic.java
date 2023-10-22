package model;

import java.util.ArrayList;

public class Epic extends Task {
    private  ArrayList<SubTask> subTasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, TaskType.EPIC);
    }
    public Epic(int id, TaskType taskType, String name, Status status, String description) {
        super(id, taskType, name, status, description);
    }

        public void setSubTasks(SubTask subTask) {
        subTasks.add(subTask);
    }

    public void setSubTask(ArrayList<SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void removeSubTask(int id) {
        subTasks.remove(id);
    }


    public void deleteSubTask() {
        subTasks.clear();
    }


}
