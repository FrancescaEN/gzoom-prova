package it.mapsgroup.gzoom.security.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class KeycloakAdminClient {

    @Value("${keycloak.url:}")
    private String keycloakUrl;

    @Value("${keycloak.realm:}")
    private String realm;

    @Value("${keycloak.client.id.be:}")
    private String clientId;

    @Value("${keycloak.client.id.be.secret:}")
    private String clientSecret;

    private final RestTemplate rest = new RestTemplate();

    public String getAdminToken() {
        String url = keycloakUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> req = new HttpEntity<>(body, headers);
        Map<String, Object> resp = rest.postForObject(url, req, Map.class);

        if (resp == null || !resp.containsKey("access_token")) {
            throw new RuntimeException("Failed to retrieve access token from Keycloak");
        }

        return resp.get("access_token").toString();
    }

    public String getUsernameFromSub(String sub) {
        String token = getAdminToken();

        String url = keycloakUrl + "/admin/realms/" + realm + "/users/" + sub;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> req = new HttpEntity<>(headers);

        ResponseEntity<Map> response = rest.exchange(
                url,
                HttpMethod.GET,
                req,
                Map.class
        );

        Map body = response.getBody();
        if (body == null) return null;

        return (String) body.get("username");
    }
}

