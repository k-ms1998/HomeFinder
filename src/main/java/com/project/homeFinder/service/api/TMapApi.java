package com.project.homeFinder.service.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.homeFinder.dto.request.TMapTransitRoutesSubRequest;
import com.project.homeFinder.dto.response.raw.TMapTransitRoutesSubRawResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TMapApi {

    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${sk.transit.api.key}")
    private String APP_KEY;

    public TMapTransitRoutesSubRawResponse calculateRouteTmapTransit(TMapTransitRoutesSubRequest request) throws IOException, InterruptedException {
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
                .header("appKey", APP_KEY)
                .method("POST", HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
        Map<String, Object> attributes = mapper.readValue(response.body(), new TypeReference<>(){});

        return TMapTransitRoutesSubRawResponse.from(attributes);
    }
}
