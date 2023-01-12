package com.project.homeFinder.service;

import com.project.homeFinder.dto.request.TMapTransitRoutesSubRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RouteServiceTest {

    private final RouteService routeService;

    public RouteServiceTest(@Autowired RouteService routeService) {
        this.routeService = routeService;
    }

    @Test
    void givenNothing_whenRequestingTmapTransitRoute_thenSuccess() throws Exception {
        // Given

        // When & Then
        routeService.calculateRouteTmapTransit(createTMapTransitRoutesSubRequest());

    }

    private TMapTransitRoutesSubRequest createTMapTransitRoutesSubRequest() {
        return TMapTransitRoutesSubRequest.of(
                "126.926493082645",
                "37.6134436427887",
                "127.126936754911",
                "37.5004198786564"
        );

    }

}