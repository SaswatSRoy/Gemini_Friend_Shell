package com.saswat.GeminiShell.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.saswat.GeminiShell.config.GeminiConfiguration;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class GeminiService {
    private final GeminiConfiguration apiConfig;
    private final HttpClient httpClient;
    private final List<Map<String, Object>> history = new ArrayList<>();
    private final ObjectMapper objectMapper;

    public GeminiService(GeminiConfiguration apiConfig, HttpClient httpClient, ObjectMapper objectMapper) {
        this.apiConfig = apiConfig;
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    public String generateResponse(String userInput, String initialGreeting) {
        if (history.isEmpty()) {
            String systemPrompt = "You are a friendly AI assistant on a Linux desktop. The user is Saswat. Start the conversation by saying: '" + initialGreeting + "' Then, ask an open-ended question. Keep responses concise.";
            history.add(Map.of("role", "user", "parts", List.of(Map.of("text", systemPrompt))));
        } else {
            history.add(Map.of("role", "user", "parts", List.of(Map.of("text", userInput))));
        }

        try {

            String requestBody = buildApiRequestBody();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiConfig.endpoint() + "?key=" + apiConfig.key()))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());


            String modelResponseText = parseApiResponse(response.body());
            history.add(Map.of("role", "model", "parts", List.of(Map.of("text", modelResponseText))));

            return modelResponseText;
        } catch (Exception e) {
            return "Error: Could not connect to the AI service. " + e.getMessage();
        }
    }

    // Helper method to build the JSON body from the 'history' list
    private String buildApiRequestBody() throws Exception {
        Map<String, Object> requestBody = Map.of("contents", this.history);
        return objectMapper.writeValueAsString(requestBody);
    }

    // Helper method to parse the JSON response and extract the text
    private String parseApiResponse(String responseBody) throws Exception {

        Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);

        return Optional.ofNullable(responseMap)
                .map(res -> (List<Map<String, Object>>) res.get("candidates"))
                .filter(list -> !list.isEmpty())
                .map(list -> list.get(0))
                .map(candidate -> (Map<String, Object>) candidate.get("content"))
                .map(content -> (List<Map<String, Object>>) content.get("parts"))
                .filter(list -> !list.isEmpty())
                .map(list -> list.get(0))
                .map(part -> (String) part.get("text"))
                .orElse("Sorry, I couldn't get a response.");
    }
}

