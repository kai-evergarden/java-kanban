public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        taskManager.addTask("Build a house", "hellow owrld", "NEW");
        taskManager.addEpic("build a house", "build", "NEW");
        taskManager.addSubTask(2, "buy wood", "aaaa", "NEW");
        taskManager.addSubTask(2, "hello world", "aaaa", "NEW");
    }
}
