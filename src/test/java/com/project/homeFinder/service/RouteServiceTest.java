package com.project.homeFinder.service;

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
        routeService.calculateRouteTmapTransit();

    }

}