package controllers;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private static final byte SIZE = 10;
    private final List<Task> history = new ArrayList<>();
    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(List.copyOf(history));
    }

    @Override
    public void add(Task task) {
        if(task == null) System.exit(1);
        if(history.size() == SIZE) {
            history.remove(0);
        }
        history.add(task);
    }
}
