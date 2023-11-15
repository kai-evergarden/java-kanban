package controllers.KV;

import exceptions.ManagerSaveException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.IntToDoubleFunction;

public class KVTaskClient {
    private HttpClient httpClient;
    private String url;
    private String API_TOKEN;


    public KVTaskClient(String url) {
        httpClient = HttpClient.newHttpClient();
        this.url = url;
        API_TOKEN = register();
    }

    public void put(String key, String json) {
        try {
            final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
            System.out.println(register());
            String apiUrl = url + "save/" + key + "?API_TOKEN=" + API_TOKEN;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl)).
                    POST(body).
                    build();
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("cannot save file to server");
        }
    }

    public String load(String key) {
        try {
            String apiUrl = url + "load/" + key + "?API_TOKEN=" + API_TOKEN;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            }
            return response.body();
//            } else {
//                throw new ManagerSaveException("status code should be 200(OK)");
//            }
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("cannot load file from server");
        }
    }

    public String register() {
        try {
            String apiUrl = url + "register";
//            System.out.println(url);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                throw new ManagerSaveException("status code should me 200(OK)");
            }
        } catch (IOException | InterruptedException e) {
            e.getStackTrace();
            throw new ManagerSaveException("cannot get api token");
        }
    }


}
