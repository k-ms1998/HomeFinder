package com.project.homeFinder.service;

import com.project.homeFinder.dto.Point;
import com.project.homeFinder.dto.request.*;
import com.project.homeFinder.dto.response.*;
import com.project.homeFinder.dto.response.raw.TMapTransitRoutesSubRawResponse;
import com.project.homeFinder.service.api.KakaoApi;
import com.project.homeFinder.service.api.TMapApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RouteService {

    private final TMapApi tMapApi;
    private final KakaoApi kakaoApi;

    public MultipleDestinationResponse toMultipleDestinations(MultipleDestinationsRequest request) {
        String startX = request.getStart().getX();
        String startY = request.getStart().getY();

        int size = 0;
        List<MultipleDestinationResponseBody> body = new ArrayList<>();
        for (Point destination : request.getDestinations()) {
            String endX = destination.getX();
            String endY = destination.getY();

            try {
                TMapTransitRoutesSubRawResponse response = tMapApi.calculateRouteTmapTransit(TMapTransitRoutesSubRequest.of(startX, startY, endX, endY));
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
                    = tMapApi.calculateRouteTmapTransit(TMapTransitRoutesSubRequest.of(startX, startY, endX, endY));
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
}
