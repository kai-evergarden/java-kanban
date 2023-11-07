import controllers.*;
import exceptions.TimeCrossingException;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Main {
    public static final String FILE = new File(System.getProperty("user.dir")) +
            File.separator + "resources" + File.separator + "config.csv";

    public static void main(String[] args) throws TimeCrossingException {
        var test = FileBackedTasksManager.loadFromFile(FILE);
        test.addEpic(new Epic("test", "test"));
//        System.out.println(test.getListOfEpics());
        test.addSubTask(new SubTask("test", "test", 1, LocalDateTime.now().plusHours(1), 60));
    }


}
