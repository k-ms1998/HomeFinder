package com.project.homeFinder.controller;

import com.project.homeFinder.domain.BusStation;
import com.project.homeFinder.service.BusStationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bus_station")
public class BusStationController {

    private final BusStationService busStationService;

    @GetMapping("/read_and_save")
    public int readFileAndSaveBusStationInfo() {
        List<BusStation> result = busStationService.readFileAndSaveBusStationInfo();

        return result.size();
    }
}
