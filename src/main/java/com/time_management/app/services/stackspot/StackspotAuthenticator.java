package com.time_management.app.services.stackspot;

import com.jayway.jsonpath.JsonPath;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class StackspotAuthenticator {

    private static final String TOKEN_URL = "https://idm.stackspot.com/zup/oidc/oauth/token";
    private static final String CLIENT_ID = "4869b6df-aed6-4f04-a55c-93bc7a8113fb";
    private static final String CLIENT_SECRET = "a07usf26FyKITBXChLE9YO1lJ349IBH5sbDA3uS029r5nY7eAow4fb3hIEG28OLq";
    private static final String GRANT_TYPE = "client_credentials";

    private final RestTemplate restTemplate;

    public StackspotAuthenticator(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String authenticate() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            String requestBody = String.format(
                    "client_id=%s&grant_type=%s&client_secret=%s",
                    CLIENT_ID, GRANT_TYPE, CLIENT_SECRET
            );

            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            String response = restTemplate.postForObject(TOKEN_URL, request, String.class);

            return JsonPath.parse(response).read("$.access_token", String.class);

        } catch (Exception e) {
            throw new RuntimeException("Stackspot authenticator error", e);
        }
    }
}