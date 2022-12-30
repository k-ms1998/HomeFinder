package com.project.homeFinder.service;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.service.invoker.HttpRequestValues;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;

@Service
public class RouteService {

    public void calculateRouteTmapTransit() throws IOException, InterruptedException {
        RestTemplate restTemplate = new RestTemplate();

        StringBuilder body = new StringBuilder();
        body.append("{")
                .append("\"startX\"").append(":").append("\"126.926493082645\"").append(",")
                .append("\"startY\"").append(":").append("\"37.6134436427887\"").append(",")
                .append("\"endX\"").append(":").append("\"127.126936754911\"").append(",")
                .append("\"endY\"").append(":").append("\"37.5004198786564\"")
                .append("}");
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://apis.openapi.sk.com/transit/routes"))
                .header("accept", "application/json")
                .header("content-type", "application/json")
                .header("appKey", "INSERT VALID APP KEY") //TODO: 키를 노출시키지 않기
                .method("POST", HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("response = " + response.body());
    }



}
