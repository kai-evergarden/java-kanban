package controllers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private HttpClient httpClient;
    private KVServer kvServer;


    private String url;


    public KVTaskClient(String url) throws IOException {
        httpClient = HttpClient.newHttpClient();
        kvServer = new KVServer();
        this.url = url;
        kvServer.start();
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        String apiUrl = url + "save/" + key + "?API_TOKEN=" + register();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl)).
                POST(body).
                build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public String load(String key) throws IOException, InterruptedException {
        String apiUrl = url + "load/" + key + "?API_TOKEN=" + register();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() == 200) {
            return response.body();
        } else {
            return null;
        }
    }

    public String register() throws IOException, InterruptedException {
        String apiUrl = url + "register";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() == 200) {
            return response.body();
        } else {
            return null;
        }
    }


}
