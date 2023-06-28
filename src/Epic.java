import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<SubTask> subTasks = new ArrayList<>();

    public Epic(String name, String description, String status) {
        super(name, description, status);
    }

    public void setSubTasks(SubTask subTask) {
        subTasks.add(subTask);
    }

    public void printTask(){
        System.out.println(subTasks);
    }
}
