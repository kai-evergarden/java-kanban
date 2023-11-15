package controllers.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import controllers.managers.FileBackedTasksManager;
import controllers.managers.Managers;
import controllers.interfaces.TaskManager;
import exceptions.IdDoesntExistException;
import exceptions.ManagerSaveException;
import model.Epic;
import model.SubTask;
import model.Task;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.regex.Pattern;



public class HttpTaskServer {

    public static final int PORT = 8080;
    public static final String FILE = new File(System.getProperty("user.dir")) +
            File.separator + "resources" + File.separator + "config.csv";
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String DELETE = "DELETE";


    private HttpServer httpServer;

    private Gson gson;

    private TaskManager taskManager;

    public HttpTaskServer() throws IOException {
        httpServer = HttpServer.create();
        gson = Managers.getGson();
        taskManager = FileBackedTasksManager.loadFromFile(FILE);
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", this::tasksHandler);
    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
    }

    //как избавится от миддиарда ифов
    public void tasksHandler(HttpExchange httpExchange) {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String requestMethod = httpExchange.getRequestMethod();
            switch (requestMethod) {
                case GET:
                    try {
                        if (Pattern.matches("^/tasks/task/*$", path)) {
                            httpExchange.sendResponseHeaders(200, 0);
                            writeResponse(httpExchange, gson.toJson(taskManager.getTaskById(getIdFromPath(path))));
                        } else if (Pattern.matches("^/tasks/subtask/*$", path)) {
                            httpExchange.sendResponseHeaders(200, 0);
                            writeResponse(httpExchange, gson.toJson(taskManager.getSubtaskById(getIdFromPath(path))));
                        } else if (Pattern.matches("^/tasks/epic/*$", path)) {
                            httpExchange.sendResponseHeaders(200, 0);
                            writeResponse(httpExchange, gson.toJson(taskManager.getEpicById(getIdFromPath(path))));
                        } else if (Pattern.matches("^/tasks/task$", path)) {
                            httpExchange.sendResponseHeaders(200, 0);
                            writeResponse(httpExchange, gson.toJson(taskManager.getListOfTasks()));
                        } else if (Pattern.matches("^/tasks/subtask$", path)) {
                            httpExchange.sendResponseHeaders(200, 0);
                            writeResponse(httpExchange, gson.toJson(taskManager.getListOfSubTasks()));
                        } else if (Pattern.matches("^/tasks/epic$", path)) {
                            httpExchange.sendResponseHeaders(200, 0);
                            writeResponse(httpExchange, gson.toJson(taskManager.getListOfEpics()));
                        } else if (Pattern.matches("^/tasks/subtask/epic/*$", path)) {
                            httpExchange.sendResponseHeaders(200, 0);
                            writeResponse(httpExchange, gson.toJson(taskManager.getListOfSubTasksOfEpic(
                                    taskManager.getEpicById(getIdFromPath(path)))));
                        } else if (Pattern.matches("^/tasks/history$", path)) {
                            httpExchange.sendResponseHeaders(200, 0);
                            writeResponse(httpExchange, gson.toJson(taskManager.
                                    getHistoryManager().getHistory()));
                        } else if (Pattern.matches("^/tasks$", path)) {
                            httpExchange.sendResponseHeaders(200, 0);
                            writeResponse(httpExchange, gson.toJson(taskManager.getPrioritizedTasks()));
                        } else {
                            httpExchange.sendResponseHeaders(404, 0);
                            writeResponse(httpExchange, "link doesn't exist");
                        }
                        break;
                    } catch (IdDoesntExistException e) {
                        httpExchange.sendResponseHeaders(404, 0);
                        writeResponse(httpExchange, "Id doesnt't exist");
                    }
                case POST:
                    if (Pattern.matches("^/tasks/task$", path)) {
                        Task task = gson.fromJson(getJsonFromRequest(httpExchange), Task.class);
                        taskManager.addTask(task);
                        httpExchange.sendResponseHeaders(200, 0);
                    } else if (Pattern.matches("^/tasks/subtask$", path)) {
                        SubTask subTask = gson.fromJson(getJsonFromRequest(httpExchange), SubTask.class);
                        System.out.println(taskManager.getListOfEpics());
                        taskManager.addSubTask(subTask);
                        httpExchange.sendResponseHeaders(200, 0);
                    } else if (Pattern.matches("^/tasks/epic$", path)) {
                        Epic epic = gson.fromJson(getJsonFromRequest(httpExchange), Epic.class);
                        taskManager.addEpic(epic);
                        httpExchange.sendResponseHeaders(200, 0);
                    } else {
                        httpExchange.sendResponseHeaders(404, 0);
                        writeResponse(httpExchange, "link doesn't exist");
                    }
                    break;
                case DELETE:
                    if (Pattern.matches("^/tasks/task/*$", path)) {
                        taskManager.deleteTaskById(getIdFromPath(path));
                        httpExchange.sendResponseHeaders(200, 0);
                    } else if (Pattern.matches("^/tasks/subtask/*$", path)) {
                        taskManager.deleteSubTaskById(getIdFromPath(path));
                        httpExchange.sendResponseHeaders(200, 0);
                    } else if (Pattern.matches("^/tasks/epic/*$", path)) {
                        System.out.println(getIdFromPath(path));
                        taskManager.deleteEpicById(getIdFromPath(path));
                        httpExchange.sendResponseHeaders(200, 0);
                    } else if (Pattern.matches("^/tasks/task$", path)) {
                        taskManager.deleteTasks();
                        httpExchange.sendResponseHeaders(200, 0);
                    } else if (Pattern.matches("^/tasks/subtask$", path)) {
                        taskManager.deleteSubTasks();
                        httpExchange.sendResponseHeaders(200, 0);
                    } else if (Pattern.matches("^/tasks/epic$", path)) {
                        taskManager.deleteEpicTasks();
                        httpExchange.sendResponseHeaders(200, 0);
                    } else {
                        httpExchange.sendResponseHeaders(404, 0);
                        writeResponse(httpExchange, "Link doesn't exist");
                    }
                    break;
                default:
                    httpExchange.sendResponseHeaders(402, 0);
                    break;
            }
        } catch (IOException e) {
            throw new ManagerSaveException("cannot send response");
        } finally {
            httpExchange.close();
        }

    }

    public int getIdFromPath(String path) {
        String[] pathArr = path.split("/");
        int index = pathArr.length - 1;
        int res = Integer.parseInt(pathArr[index].substring(1, index));
        if (res < 0)
            throw new IllegalArgumentException("Wrong id");
        else if (!isIdExist(res)) {
            throw new IdDoesntExistException("Id doesnt exist");
        } else {
            return res;
        }

    }

    public boolean isIdExist(int id) {
        Optional<Task> check = taskManager.getPrioritizedTasks().stream()
                .filter(task -> task.getId() == id)
                .findFirst();
          return check.isPresent();
    }

    public String getJsonFromRequest(HttpExchange exchange) {
        try {
            return new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.getStackTrace();
        }
        return "";
    }

    public static void writeResponse(HttpExchange httpExchange, String response) {
        try (var os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        } catch (IOException e) {
            System.out.println("cannot write response for string: " + response);
            e.getStackTrace();
        }
    }


}
