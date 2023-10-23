package controllers;

import exceptions.ManagerSaveException;
import model.Status;
import model.SubTask;
import model.Task;
import model.Epic;
import model.TaskType;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager {
    public static final String FILE = new File(System.getProperty("user.dir")) +
            File.separator + "resources" + File.separator + "config.csv";

    private void save() {
        try (Writer fileWriter = new FileWriter(FILE)) {
            fileWriter.write("id,type,name,status,description,epic\n");
            for(Task task : taskMap.values())  {
                fileWriter.write(toString(task));
            }
            for(Epic epic : epicMap.values()) {
                fileWriter.write(toString(epic));
            }
            for(SubTask subTask : subTaskMap.values()) {
                fileWriter.write(toString(subTask));
            }
            fileWriter.write("\n");
            fileWriter.write(historyToString(getHistoryManager()));
        } catch (IOException e) {
            throw new ManagerSaveException("Error writing to file");
        }
    }

    private String toString(Task task) {
        var sb = new StringBuilder();
        sb.append(task.getId()).append(",");
        sb.append(task.getTaskType()).append(",");
        sb.append(task.getName()).append(",");
        sb.append(task.getStatus()).append(",");
        sb.append(task.getDescription()).append(",");
        if(task.getTaskType() == TaskType.SUBTASK)
            sb.append(((SubTask)task).getEpicId());
        sb.append("\n");
        return sb.toString();
    }

    public String historyToString(HistoryManager manager) {
        var sb = new StringBuilder(" ");
        List<Task> history = manager.getHistory();
        for(Task task : history) {
           sb.append(task.getId()).append(",");
        }
        return sb.toString();
    }

    public List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();
       String fix =  value.trim();
        String[] arr = fix.split(",");
        for (String s : arr) {
            history.add(Integer.parseInt(s));
        }
        return history;
    }

    private Task fromString(String value) {
        String[] arr = value.split(",");
        switch(TaskType.valueOf(arr[1])) {
            case TASK:
                return new Task(Integer.parseInt(arr[0]), TaskType.valueOf(arr[1]), arr[2], Status.valueOf(arr[3]), arr[4]);
            case SUBTASK:
                return new SubTask(Integer.parseInt(arr[0]), TaskType.valueOf(arr[1]), arr[2], Status.valueOf(arr[3]), arr[4], Integer.parseInt(arr[5]));
            case EPIC:
                return new Epic(Integer.parseInt(arr[0]), TaskType.valueOf(arr[1]), arr[2], Status.valueOf(arr[3]), arr[4]);
        }
        return null;
    }

    public void fillHistory(List<Integer> ids) {
       var historyManager = getHistoryManager();
       for(Integer id : ids)  {
           historyManager.add(getTaskById(id));
       }
    }

    public static FileBackedTasksManager  loadFromFile(String file) {
        var fbtm = new FileBackedTasksManager();
        try {
            var sb = new StringBuilder(Files.readString(Path.of(file)));
            final String SEP = "\n";
            sb.delete(0, sb.indexOf(SEP) + 1);
            while(sb.charAt(0) != '\n') {
                String line = sb.substring(0, sb.indexOf(SEP));
                String[] currLine = line.split(",");
                var task = fbtm.fromString(line);
                switch(TaskType.valueOf(currLine[1])) {
                    case TASK:
                        fbtm.taskMap.put(task.getId(), task);
                        fbtm.setId(task.getId());
                        break;
                    case EPIC:
                        fbtm.epicMap.put(task.getId(), (Epic)task);
                        fbtm.setId(task.getId());
                        break;
                    case SUBTASK:
                        fbtm.subTaskMap.put(task.getId(), (SubTask) task);
                        var epic = fbtm.getEpicById(Integer.parseInt(currLine[5]));
                        epic.setSubTasks((SubTask)task);
                        fbtm.setId(task.getId());
                        break;
                }
                sb.delete(0, sb.indexOf(SEP) + 1);
            }
            sb.deleteCharAt(0);
            if(!sb.toString().isEmpty() && !sb.toString().equals("\n"))
                fbtm.fillHistory(fbtm.historyFromString(sb.toString()));
        } catch (IOException e){
           throw new ManagerSaveException("Error reading from file");
        }
        return fbtm;
    }

    @Override
    public void addTask(Task task)  {
        super.addTask(task);
        save();
    }

    @Override
    public void addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteSubTaskById(int id) {
        super.deleteSubTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        var value = super.getTaskById(id);
        save();
        return value;
    }

    @Override
    public SubTask getSubtaskById(int id) {
        var value = super.getSubtaskById(id);
        save();
        return value;
    }

    @Override
    public Epic getEpicById(int id) {
        var value = super.getEpicById(id);
        save();
        return value;
    }

    @Override
    public void changeTask(Task task) {
        super.changeTask(task);
        save();
    }

    @Override
    public void changeEpic(Epic epic) {
        super.changeEpic(epic);
        save();
    }

    @Override
    public void changeSubTask(SubTask subTask) {
       super.changeSubTask(subTask);
       save();
    }

    @Override
    public void changeSubTaskStatus(SubTask subTask, Status status) {
        super.changeSubTaskStatus(subTask, status);
        save();
    }

    public static void main(String[] args) {
        try {
            var taskManager = FileBackedTasksManager.loadFromFile(FILE);
//            System.out.println(TestFile);
            Task task1 = new Task("FirstTask", "test for first task");
            Task task2 = new Task("SecondTask", "test for second task");
            Task task3 = new Task("ThirdTask", "test for third task");
            Epic epic1 = new Epic("EPIC", "bleba");
            SubTask subTask = new SubTask("SubTask for epic with id 4", "mega subtask", 4);
            SubTask subTask1 = new SubTask("SubTask 1 for epic with id 4", "mega subtask", 4);
            taskManager.addTask(task1);
            taskManager.addTask(task2);
            taskManager.addTask(task3);
            taskManager.addEpic(epic1);
            taskManager.addSubTask(subTask);
            taskManager.addSubTask(subTask1);
            Task task = new Task("BlebaTask", "test for new version of task");
            SubTask subTask2 = new SubTask("SubTask 2 for epic with id 4", "mega subtask", 4);
            taskManager.addTask(task) ;
            taskManager.addSubTask(subTask2);
            taskManager.getEpicById(4);
            taskManager.getTaskById(1);
            taskManager.getTaskById(2);
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }

    }
}
