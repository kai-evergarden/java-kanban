package controllers;

import controllers.InMemoryHistoryManager;
import controllers.InMemoryTaskManager;

public class Managers {
    public static InMemoryTaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
