package controllers.managers;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controllers.adapters.LocalDateTimeAdapter;
import controllers.http.HttpTaskServer;
import controllers.interfaces.HistoryManager;
import controllers.interfaces.TaskManager;
import controllers.managers.FileBackedTasksManager;
import controllers.managers.InMemoryHistoryManager;
import controllers.managers.InMemoryTaskManager;

import java.io.IOException;
import java.time.LocalDateTime;

public class Managers {
    public static TaskManager getDefault() throws IOException {
        return  new HttpTaskManager("http://localhost:8078/");
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getBackend() {
        return new FileBackedTasksManager();
    }


    public static Gson getGson() {
       return  new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

}
