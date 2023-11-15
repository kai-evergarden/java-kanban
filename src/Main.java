import controllers.managers.FileBackedTasksManager;
import exceptions.TimeCrossingException;
import model.Epic;

import java.io.File;


public class Main {
    public static final String FILE = new File(System.getProperty("user.dir")) +
            File.separator + "resources" + File.separator + "config.csv";

    public static void main(String[] args) throws TimeCrossingException {
        var test = FileBackedTasksManager.loadFromFile(FILE);
        test.addEpic(new Epic("test", "test"));
    }


}
