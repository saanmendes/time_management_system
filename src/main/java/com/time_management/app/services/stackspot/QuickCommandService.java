package com.time_management.app.services.stackspot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.time_management.app.dtos.reports.SuggestionDTO;
import com.time_management.app.dtos.tasks.TaskRequestDTO;
import com.time_management.domain.models.Task;
import com.time_management.infra.output.entities.ReportEntity;
import com.time_management.infra.output.entities.TaskEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class QuickCommandService {

    private static final String CALLBACK_URL = "https://genai-code-buddy-api.stackspot.com/v1/quick-commands/callback/";
    private static final String CATEGORY_URL = "https://genai-code-buddy-api.stackspot.com/v1/quick-commands/create-execution/task-prioritization-and-optimization";
    private static final String OPTIMIZATION_URL = "https://genai-code-buddy-api.stackspot.com/v1/quick-commands/create-execution/suggest-optimization";
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

            return JsonPath.read(response.getBody(), "$.result");
        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred: " + e.getMessage();
        }
    }

    public String executeOptimizationQuickCommand(String accessToken, ReportEntity reportEntity) {
        try {
            // Configurar os cabeçalhos
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", AUTH_TOKEN + accessToken);
            headers.set("Content-Type", "application/json");

            // Corpo da requisição
            String jsonInput = String.format("{\"input_data\": \"%s\"}", reportEntity);
            System.out.println(jsonInput);

            // Criar a entidade HTTP
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonInput, headers);

            // Fazer a requisição POST
            String response = restTemplate
                    .postForObject(
                            OPTIMIZATION_URL,
                            requestEntity,
                            String.class
                    );

            // Remover aspas da resposta, se existirem
            if (response != null) {
                response = response.replace("\"", ""); // Remove todas as aspas
            }

            return response;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public SuggestionDTO getQuickCommandCallbackAsDTO(String accessToken, String executionId) {
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

            // Extrair o corpo da resposta
            String responseBody = response.getBody();
            if (responseBody == null) {
                throw new RuntimeException("Response body is null");
            }

            // Usar JsonPath para acessar a propriedade 'result'
            String result = JsonPath.read(responseBody, "$.result");

            // Converter o resultado para um Map
            Map<String, Object> dados = objectMapper.readValue(result, Map.class);

            System.out.println(dados);

            // Converter a resposta JSON para SuggestionDTO
            return objectMapper.readValue(result, SuggestionDTO.class);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("An error occurred while fetching the callback: " + e.getMessage());
        }
    }
}