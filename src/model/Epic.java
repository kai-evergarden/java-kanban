package model;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<SubTask> subTasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public void setSubTasks(SubTask subTask) {
        subTasks.add(subTask);
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
