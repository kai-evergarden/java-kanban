public class SubTask extends Task {
    private int epicId;
    private int id;

    public SubTask(String name, String description, String status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }


}
