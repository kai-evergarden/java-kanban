package test;

import com.google.gson.Gson;
import controllers.KV.KVServer;
import controllers.http.HttpTaskServer;
import controllers.interfaces.TaskManager;
import controllers.managers.Managers;
import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class HttpTaskServerTest {
    static KVServer kvServer;
    TaskManager taskManager;
    HttpResponse<String> response;
    HttpClient client = HttpClient.newHttpClient();

    HttpTaskServer httpTaskServer = new HttpTaskServer();
    Gson gson = Managers.getGson();

    void HttpTaskManagerTest() throws IOException {
    }

    public HttpTaskServerTest() throws IOException {
    }

    @BeforeAll
    public static void startKV() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
    }

    @BeforeEach
    public void setup() throws IOException {
        httpTaskServer.start();
    }

    @AfterEach
    public void stopSetup() {
        httpTaskServer.stop();
    }

    @AfterAll
    public static void stopKV() {
        kvServer.stop();
    }


    @Test
    public void testTask() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/task");

        String sendTask = gson.toJson(new Task("hello", "test", LocalDateTime.now(), 60));
        HttpRequest postRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(sendTask))
                .header("Content-type", "application/json")
                .uri(uri)
                .build();
        response = client.send(postRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        Assertions.assertEquals(200, response.statusCode());

        HttpRequest getRequest = HttpRequest.newBuilder()
                .GET()
                .header("Accept", "application/json")
                .uri(uri)
                .build();
        response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertNotNull(response.body());
        Assertions.assertEquals(200, response.statusCode());

        URI getIdUri = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest getIdRequest = HttpRequest.newBuilder()
                .GET()
                .header("Accept", "application/json")
                .uri(getIdUri)
                .build();
        response = client.send(getIdRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertNotNull(response.body());
        Assertions.assertEquals(200, response.statusCode());


    }

    @Test
    public void testEpic() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/epic");

        String sendTask = gson.toJson(new Epic("hello", "test"));
        HttpRequest postRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(sendTask))
                .header("Content-type", "application/json")
                .uri(uri)
                .build();
        response = client.send(postRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        Assertions.assertEquals(200, response.statusCode());

        HttpRequest getRequest = HttpRequest.newBuilder()
                .GET()
                .header("Accept", "application/json")
                .uri(uri)
                .build();
        response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertNotNull(response.body());
        Assertions.assertEquals(200, response.statusCode());

        URI getIdUri = URI.create("http://localhost:8080/tasks/epic/?id=1");
        HttpRequest getIdRequest = HttpRequest.newBuilder()
                .GET()
                .header("Accept", "application/json")
                .uri(getIdUri)
                .build();
        response = client.send(getIdRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertNotNull(response.body());
        Assertions.assertEquals(200, response.statusCode());



    }

    @Test
    public void testSubTask() throws IOException, InterruptedException {

        URI uriEpic = URI.create("http://localhost:8080/tasks/epic");

        var epic = new Epic("hello", "test");
        String sendEpic = gson.toJson(epic);
        HttpRequest postRequestEpic = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(sendEpic))
                .header("Content-type", "application/json")
                .uri(uriEpic)
                .build();
        client.send(postRequestEpic, HttpResponse.BodyHandlers.discarding());
        URI uri = URI.create("http://localhost:8080/tasks/subtask");

        String sendTask = gson.toJson(new SubTask("hello", "test", 1, LocalDateTime.now(), 60));
        HttpRequest postRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(sendTask))
                .header("Content-type", "application/json")
                .uri(uri)
                .build();
        response = client.send(postRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        Assertions.assertEquals(200, response.statusCode());

        HttpRequest getRequest = HttpRequest.newBuilder()
                .GET()
                .header("Accept", "application/json")
                .uri(uri)
                .build();
        response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertNotNull(response.body());
        Assertions.assertEquals(200, response.statusCode());

    }


}