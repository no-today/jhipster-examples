package io.github.notoday.bff.service;

import org.apache.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @author no-today
 * @date 2021/10/14 11:39 AM
 */
@Service
public class UserService {

    private HttpClient httpClient = HttpClient.newHttpClient();

    public String getUserImage(String uid) {
        HttpRequest request = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create("https://source.unsplash.com/random"))
            .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == HttpStatus.SC_MOVED_TEMPORARILY) {
                return response.headers().firstValue("Location").orElse(null);
            }

            return null;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
