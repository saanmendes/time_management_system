package com.time_management.app.services.stackspot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.time_management.app.dtos.tasks.TaskRequestDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class QuickCommandService {

    private static final String CALLBACK_URL = "https://genai-code-buddy-api.stackspot.com/v1/quick-commands/callback/";
    private static final String CATEGORY_URL = "https://genai-code-buddy-api.stackspot.com/v1/quick-commands/create-execution/task-prioritization-and-optimization";
    private static final String AUTH_TOKEN = "Bearer ";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public QuickCommandService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public String executeCategoryQuickCommand(String accessToken, TaskRequestDTO taskRequestDTO) {
        try {
            // Configurar os cabeçalhos
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", AUTH_TOKEN + accessToken);
            headers.set("Content-Type", "application/json");

            // Corpo da requisição
            String jsonInput = String.format("{\"input_data\": \"%s\"}", taskRequestDTO);

            System.out.println(jsonInput);

            // Criar a entidade HTTP
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonInput, headers);

            // Fazer a requisição POST
            ResponseEntity<String> response = restTemplate.exchange(
                    CATEGORY_URL,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            // Usar JsonPath para extrair o valor desejado diretamente da resposta
            String executeId = JsonPath.read(response.getBody(), "$.execution_id");
            return executeId;
        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred: " + e.getMessage();
        }
    }

    public String getQuickCommandCallback(String accessToken, String executionId) {
        try {
            // Configurar os cabeçalhos
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", AUTH_TOKEN + accessToken);

            // Criar a entidade HTTP
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            // Fazer a requisição GET
            ResponseEntity<String> response = restTemplate.exchange(
                    CALLBACK_URL + executionId,
                    HttpMethod.GET,
                    requestEntity,
                    String.class
            );

            // Retornar a resposta
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred: " + e.getMessage();
        }
    }
}