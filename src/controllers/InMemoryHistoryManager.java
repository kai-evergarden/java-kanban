package controllers;

import model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager{
    private final ArrayList<Task> history = new ArrayList<>();
    @Override
    public ArrayList<Task> getHistory() {
        while(history.size() > 10) {
            history.remove(1);
        }
        return history;
    }

    @Override
    public void add(Task task) {
        history.add(task);
    }
}
