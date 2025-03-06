package com.time_management.app.services.stackspot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;
import com.time_management.app.dtos.tasks.TaskRequestDTO;
import com.time_management.infra.output.entities.ReportEntity;
import com.time_management.infra.output.entities.TaskEntity;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
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
            String response = restTemplate
                    .postForObject
                            (CATEGORY_URL,
                                    requestEntity,
                                    String.class);

            // Remover aspas da resposta, se existirem
            if (response != null) {
                response = response.replace("\"", ""); // Remove todas as aspas
            }

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred: " + e.getMessage();
        }
    }

    public String executePriorizationQuickCommand(String accessToken, TaskRequestDTO taskRequestDTO) {
        try {
            // Configurar os cabeçalhos
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", AUTH_TOKEN + accessToken);
            headers.set("Content-Type", "application/json");

            // Serializar o objeto TaskRequestDTO para JSON
            String jsonInput = objectMapper.writeValueAsString(taskRequestDTO);

            // Criar a entidade HTTP
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonInput, headers);

            // Fazer a requisição POST
            String response = restTemplate
                    .postForObject(PRIORITY_URL,
                            requestEntity,
                            String.class);

            // Remover aspas da resposta, se existirem
            if (response != null) {
                response = response.replace("\"", ""); // Remove todas as aspas
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred: " + e.getMessage();
        }
    }

    public String executeOptimizationQuickCommand(String accessToken, List<TaskEntity> taskEntities) {
        try {
            // Configurar os cabeçalhos
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", AUTH_TOKEN + accessToken);
            headers.set("Content-Type", "application/json");

            // Encapsular os dados no formato esperado pela API
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule()); // Para lidar com datas
            String jsonInput = mapper.writeValueAsString(
                    Map.of("input_data", Map.of("tasks", taskEntities))
            );

            // Criar a entidade HTTP
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonInput, headers);

            // Fazer a requisição POST
            String response = restTemplate.postForObject(OPTIMIZATION_URL, requestEntity, String.class);

            // Remover aspas da resposta, se existirem
            if (response != null) {
                response = response.replace("\"", ""); // Remove todas as aspas
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred: " + e.getMessage();
        }
    }



    public String getQuickCommandCategoryCallback(String accessToken, String executionId) {
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

            return JsonPath.read(response.getBody(), "$.result");
        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred: " + e.getMessage();
        }
    }


    public String getQuickCommandPriorityCallback(String accessToken, String executionId) {
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

            return JsonPath.read(response.getBody(), "$.result");
        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred: " + e.getMessage();
        }
    }

    public String getQuickCommandOptimizationCallback(String accessToken, String executionId) {
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

            return JsonPath.read(response.getBody(), "$.result");
        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred: " + e.getMessage();
        }
    }

}