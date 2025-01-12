package com.aluracursos.libreria.service;

import java.net.URI;
import java.net.http.*;

public class ConsumoAPI {
    public String obtenerDatos(String url) {
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
            HttpResponse<String> response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
