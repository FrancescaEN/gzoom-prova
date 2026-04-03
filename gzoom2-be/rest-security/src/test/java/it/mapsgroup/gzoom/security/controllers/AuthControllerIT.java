package it.mapsgroup.gzoom.security.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.mapsgroup.gzoom.security.dto.models.AuthRequest;
import it.mapsgroup.gzoom.security.dto.models.ExternalLoginKey;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static it.mapsgroup.gzoom.security.controllers.AuthController.GZOOM_2_APIKEY;
import static org.junit.jupiter.api.Assertions.*;

class AuthControllerIT {

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    @Test
    void getExternalKey() throws Exception {
        AuthRequest authRequest = new AuthRequest("gzoom.rodo","gzoom");
        String externalKey = sendPostRequest("http://localhost:8081/rest/api/getExternalKey",mapper.writeValueAsString(authRequest));
        System.out.println("External Login Key: " + externalKey);
        assertNotNull(externalKey);
    }

    private String sendPostRequest(String url, String jsonBody) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header(GZOOM_2_APIKEY, "mUZEPHgeMzhhtyurkmWKDNmzf2jMYwo36QTAC6yw2ftdT2Scrm")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();

        if (status >= 200 && status < 300) {
            // Converte il JSON in un oggetto Java
            ExternalLoginKey ek = mapper.readValue(response.body(), ExternalLoginKey.class);
            return ek.getExternalLoginKey();
        } else {
            throw new RuntimeException("Errore nella chiamata REST: HTTP " + status +
                    " - body: " + response.body());
        }
    }
}