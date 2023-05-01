package com.project.homeFinder.controller;

import com.project.homeFinder.dto.request.MultipleDestinationsRequest;
import com.project.homeFinder.dto.response.MultipleDestinationResponse;
import com.project.homeFinder.service.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/route")
@Tag(name = "Route", description = "Route API Documentation") // Swagger 연동
public class RouteController {

    private final RouteService routeService;

    @PostMapping
    @Operation(summary = "Fetch Transit Route to Multiple Destinations from Start Point") // Swagger 연동
    public ResponseEntity<MultipleDestinationResponse> toMultipleDestinations(@RequestBody MultipleDestinationsRequest request) {
        // 강남역: x = 127.02773729679055, y = 37.497945841837804,
        // 잠실역: x = 127.1001698951624, y = 37.51327777348752
        // 잠실리센츠: x = 127.08843979084443 , y = 37.512737526377535
        MultipleDestinationResponse response = routeService.toMultipleDestinations(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

}
