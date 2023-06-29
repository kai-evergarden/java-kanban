public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        taskManager.addTask("Build a house", "hellow owrld", "NEW");
        taskManager.addEpic("build a house", "build", "NEW");
        taskManager.addSubTask(2, "buy wood", "aaaa", "NEW");
        taskManager.addSubTask(2, "hello world", "aaaa", "DONE");
        taskManager.addEpic("destroy the door", "build", "NEW");
        taskManager.addSubTask(5, "1sub", "aaaa", "NEW");
        taskManager.addSubTask(5, "2sub", "aaaa", "NEW");
    }
}
