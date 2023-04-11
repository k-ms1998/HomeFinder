package com.project.homeFinder.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.homeFinder.dto.Point;
import com.project.homeFinder.dto.request.MultipleDestinationsRequest;
import com.project.homeFinder.dto.request.TMapTransitRoutesSubRequest;
import com.project.homeFinder.dto.response.MultipleDestinationResponse;
import com.project.homeFinder.dto.response.MultipleDestinationResponseBody;
import com.project.homeFinder.dto.response.TotalTimeAndTransferCount;
import com.project.homeFinder.dto.response.raw.TMapTransitRoutesSubRawResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteService {

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


    public MultipleDestinationResponse toMultipleDestinations(MultipleDestinationsRequest request) {
        String startX = request.getStart().getX();
        String startY = request.getStart().getY();

        int size = 0;
        List<MultipleDestinationResponseBody> body = new ArrayList<>();
        for (Point destination : request.getDestinations()) {
            String endX = destination.getX();
            String endY = destination.getY();

            try {
                TMapTransitRoutesSubRawResponse response = calculateRouteTmapTransit(TMapTransitRoutesSubRequest.of(startX, startY, endX, endY));
                List<TotalTimeAndTransferCount> collect = Arrays.stream(response.getMetaData().getPlan().getItineraries())
                        .map(i -> TotalTimeAndTransferCount.of(i.getTotalTime(), i.getTransferCount()))
                        .collect(Collectors.toList());

                Collections.sort(collect, new Comparator<TotalTimeAndTransferCount>() {
                    @Override
                    public int compare(TotalTimeAndTransferCount o1, TotalTimeAndTransferCount o2) {
                        return (int)(o1.getTotalTime() - o2.getTotalTime());
                    }
                });

                body.add(MultipleDestinationResponseBody.of(collect.size(), String.valueOf(size++), collect));

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return MultipleDestinationResponse.of(size, body);
    }
}
