package com.ol.chronoshare.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    private final String API_URL = "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash:generateContent?key=";

    public String analyzeReceipt(MultipartFile file) {
        try {
            // 1. Convert image en base64
            String base64Image = Base64.getEncoder().encodeToString(file.getBytes());

            // 2. Corps de la requête (voir doc Gemini)
            Map<String, Object> imagePart = Map.of(
                    "inlineData", Map.of(
                            "mimeType", file.getContentType(),
                            "data", base64Image
                    )
            );

            Map<String, Object> promptPart = Map.of("text", "Lis ce ticket de caisse et renvoie un JSON {titre:string, montant:number}");

            Map<String, Object> content = Map.of(
                    "parts", List.of(promptPart, imagePart)
            );

            Map<String, Object> requestBody = Map.of(
                    "contents", List.of(content)
            );

            // 3. Headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            // 4. Envoi
            ResponseEntity<Map> response = restTemplate.postForEntity(API_URL + apiKey, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                List<Map> candidates = (List<Map>) response.getBody().get("candidates");
                if (!candidates.isEmpty()) {
                    Map contentMap = (Map) candidates.get(0).get("content");
                    List<Map> parts = (List<Map>) contentMap.get("parts");
                    return (String) parts.get(0).get("text");
                }
            }

            return "Aucune réponse de Gemini";

        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur : " + e.getMessage();
        }
    }
}
