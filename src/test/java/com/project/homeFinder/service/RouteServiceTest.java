package com.project.homeFinder.service;

import com.project.homeFinder.dto.request.TMapTransitRoutesSubRequest;
import com.project.homeFinder.service.api.TMapApi;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RouteServiceTest {

    private final RouteService routeService;
    private final TMapApi tMapApi;

    public RouteServiceTest(@Autowired RouteService routeService, @Autowired TMapApi tMapApi) {
        this.routeService = routeService;
        this.tMapApi = tMapApi;
    }

    @Disabled
    @Test
    void givenNothing_whenRequestingTmapTransitRoute_thenSuccess() throws Exception {
        // Given

        // When & Then
        tMapApi.calculateRouteTmapTransit(createTMapTransitRoutesSubRequest());

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