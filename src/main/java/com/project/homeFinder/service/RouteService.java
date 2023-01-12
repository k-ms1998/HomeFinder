package com.project.homeFinder.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.homeFinder.dto.request.TMapTransitRoutesSubRequest;
import com.project.homeFinder.dto.response.raw.TMapTransitRoutesSubRawResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final ObjectMapper mapper = new ObjectMapper();

    public void calculateRouteTmapTransit(TMapTransitRoutesSubRequest request) throws IOException, InterruptedException {
        StringBuilder body = new StringBuilder();
        body.append("{")
                .append("\"startX\"").append(":").append("\"").append(request.getStartX()).append("\"").append(",")
                .append("\"startY\"").append(":").append("\"").append(request.getStartY()).append("\"").append(",")
                .append("\"endX\"").append(":").append("\"").append(request.getEndX()).append("\"").append(",")
                .append("\"endY\"").append(":").append("\"").append(request.getEndY()).append("\"").append(",")
                .append("\"format\"").append(":").append("\"").append(request.getFormat()).append("\"").append(",")
                .append("\"count\"").append(":").append("\"").append(request.getCount()).append("\"")
                .append("}");
        
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://apis.openapi.sk.com/transit/routes/sub"))
                .header("accept", "application/json")
                .header("content-type", "application/json")
                .header("appKey", "INSERT VALID APP KEY") //TODO: 키를 노출시키지 않기
                .method("POST", HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
        Map<String, Object> attributes = mapper.readValue(response.body(), new TypeReference<>(){});

        TMapTransitRoutesSubRawResponse rawResponse = TMapTransitRoutesSubRawResponse.from(attributes);
        System.out.println("rawResponse = " + rawResponse);
    }



}
