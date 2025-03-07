package com.time_management.app.services.stackspot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;
import com.time_management.app.dtos.tasks.TaskRequestDTO;
import com.time_management.infra.output.entities.TaskEntity;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class QuickCommandService {

    private static final String CALLBACK_URL = "https://genai-code-buddy-api.stackspot.com/v1/quick-commands/callback/";
    private static final String CATEGORY_URL = "https://genai-code-buddy-api.stackspot.com/v1/quick-commands/create-execution/task-prioritization-and-optimization";
    private static final String OPTIMIZATION_URL = "https://genai-code-buddy-api.stackspot.com/v1/quick-commands/create-execution/suggest-optimization";
    private static final String PRIORITY_URL = "https://genai-code-buddy-api.stackspot.com/v1/quick-commands/create-execution/task-priorization";
    private static final String AUTH_TOKEN = "Bearer ";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public QuickCommandService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public String executeCategoryQuickCommand(String accessToken, TaskRequestDTO taskRequestDTO) {
        return executePostRequest(CATEGORY_URL, accessToken, Map.of("input_data", taskRequestDTO));
    }

    public String executePriorityQuickCommand(String accessToken, TaskRequestDTO taskRequestDTO) {
        return executePostRequest(PRIORITY_URL, accessToken, taskRequestDTO);
    }

    public String executeOptimizationQuickCommand(String accessToken, List<TaskEntity> taskEntities) {
        return executePostRequest(OPTIMIZATION_URL, accessToken, Map.of("input_data", Map.of("tasks", taskEntities)));
    }

    public String getQuickCommandCategoryCallback(String accessToken, String executionId) {
        return executeGetRequest(CALLBACK_URL + executionId, accessToken);
    }

    public String getQuickCommandPriorityCallback(String accessToken, String executionId) {
        return executeGetRequest(CALLBACK_URL + executionId, accessToken);
    }

    public String getQuickCommandOptimizationCallback(String accessToken, String executionId) {
        return executeGetRequest(CALLBACK_URL + executionId, accessToken);
    }

    private String executePostRequest(String url, String accessToken, Object body) {
        try {
            HttpHeaders headers = createHeaders(accessToken);
            String jsonInput = objectMapper.writeValueAsString(body);
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonInput, headers);

            String response = restTemplate.postForObject(url, requestEntity, String.class);
            return cleanResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred: " + e.getMessage();
        }
    }

    private String executeGetRequest(String url, String accessToken) {
        try {
            HttpHeaders headers = createHeaders(accessToken);
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
            return JsonPath.read(response.getBody(), "$.result");
        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred: " + e.getMessage();
        }
    }

    private HttpHeaders createHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", AUTH_TOKEN + accessToken);
        headers.set("Content-Type", "application/json");
        return headers;
    }

    private String cleanResponse(String response) {
        return response != null ? response.replace("\"", "") : null;
    }
}