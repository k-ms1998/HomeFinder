package com.project.homeFinder.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.homeFinder.dto.Point;
import com.project.homeFinder.dto.request.*;
import com.project.homeFinder.dto.response.*;
import com.project.homeFinder.dto.response.raw.TMapTransitRoutesSubRawResponse;
import com.project.homeFinder.util.ServiceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
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

    public SingleDestinationResponse toSingleDestination(SingleDestinationRequest request) {
        String startX = request.getStart().getX();
        String startY = request.getStart().getY();
        String endX = request.getDestination().getX();
        String endY = request.getDestination().getY();

        try {
            TMapTransitRoutesSubRawResponse response
                    = calculateRouteTmapTransit(TMapTransitRoutesSubRequest.of(startX, startY, endX, endY));
            List<TotalTimeAndTransferCount> collect = Arrays.stream(response.getMetaData().getPlan().getItineraries())
                    .map(i -> TotalTimeAndTransferCount.of(i.getTotalTime(), i.getTransferCount()))
                    .collect(Collectors.toList());

            Collections.sort(collect, new Comparator<TotalTimeAndTransferCount>() {
                @Override
                public int compare(TotalTimeAndTransferCount o1, TotalTimeAndTransferCount o2) {
                    return (int)(o1.getTotalTime() - o2.getTotalTime());
                }
            });

            return SingleDestinationResponse.of(TotalTimeAndTransferCount.of(collect.get(0).getTotalTime(), collect.get(0).getTransferCount()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return SingleDestinationResponse.of(null);
    }
    
    public List<SubwayTravelTimeMultipleResponse> fetchTransitTimeFromMultiplePointsAndTime(List<PointTravelTimeRequest> pointTravelTimeRequest, SubwayService subwayService) {
        // 각 지점으로부터 가장 까가운 지하철역 찾기
        List<List<SubwayTravelTimeRequest>> request = pointTravelTimeRequest.stream()
                .map(r -> subwayService.findToNearestSubway(Point.of(r.getX(), r.getY())).stream()
                        .map(ksbcr -> SubwayTravelTimeRequest.fromKakaoSearchByCategoryResponse(ksbcr, r.getTime()))
                        .collect(Collectors.toList())
                )
                .collect(Collectors.toList());

        //각 지점으로부터 가장 가까운 지하철역들의 모든 조합들 구하기
        int destinationCount = request.size();

        if(destinationCount >= 5){ // 너무 많은 도착지점들을 입력할 경우, 서버 과부화가 일어날 수 있기 때문에 제한
            return null;
        }
        int nearestSubwayCount = destinationCount <= 2 ? 2 : 1; // 입력된 지점들에 따라서, 가까운 지하철역들의 개수 제한

        List<List<SubwayTravelTimeRequest>> subwayCombinations = new ArrayList<>();
        findSubwayCombinations(0, destinationCount, nearestSubwayCount, request, new ArrayList<>(), subwayCombinations);
        log.info("subwayCombinations: {}", subwayCombinations);

        return subwayCombinations.stream()
                .map(sc1 -> sc1.stream()
                        .map(sttr -> SubwayTravelTimeRequest.of(ServiceUtils.checkAndRemoveSubwayNameSuffix(sttr.getName()), sttr.getTime()))
                        .collect(Collectors.toList())
                )
                .map(sc2 -> subwayService.findSubwaysByTimeMultiple(sc2))
                .collect(Collectors.toList());
    }

    private void findSubwayCombinations(int depth, int targetDepth, int nearestSubwayCount, List<List<SubwayTravelTimeRequest>> request,
                                        List<SubwayTravelTimeRequest> combination, List<List<SubwayTravelTimeRequest>> subwayCombinations) {
        if(depth >= targetDepth){
            subwayCombinations.add(combination);
            return;
        }
        List<SubwayTravelTimeRequest> nearestSubways = request.get(depth);
        int size = nearestSubways.size() < nearestSubwayCount ? nearestSubways.size() : nearestSubwayCount;
        for(int i = 0; i < size; i++){
            findSubwayCombinations(depth + 1, targetDepth, nearestSubwayCount, request, copyCombination(combination, nearestSubways.get(i)), subwayCombinations);
        }
    }

    private List<SubwayTravelTimeRequest> copyCombination(List<SubwayTravelTimeRequest> combination, SubwayTravelTimeRequest next) {
        List<SubwayTravelTimeRequest> list = new ArrayList<>();
        for (SubwayTravelTimeRequest sttr : combination) {
            list.add(SubwayTravelTimeRequest.of(sttr.getName(), sttr.getX(), sttr.getY(), sttr.getTime()));
        }
        list.add(SubwayTravelTimeRequest.of(next.getName(), next.getX(), next.getY(), next.getTime()));

        return list;
    }
}
