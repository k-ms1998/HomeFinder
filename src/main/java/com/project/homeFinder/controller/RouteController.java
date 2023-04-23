package com.project.homeFinder.controller;

import com.project.homeFinder.dto.Point;
import com.project.homeFinder.dto.request.MultipleDestinationsRequest;
import com.project.homeFinder.dto.request.PointTravelTimeRequest;
import com.project.homeFinder.dto.response.KakaoSearchByCategoryResponse;
import com.project.homeFinder.dto.response.MultipleDestinationResponse;
import com.project.homeFinder.service.RouteService;
import com.project.homeFinder.service.SubwayService;
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

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/route")
@Tag(name = "Route", description = "Route API Documentation") // Swagger 연동
public class RouteController {

    private final RouteService routeService;
    private final SubwayService subwayService; // RouteService 에서 주입 시켜주면 Circular Dependency 발생 -> 컨트롤러에서 주입시켜주기

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

    /**
     *
     * @param request
     * Point: 시작 지점의 좌표
     * Time: 해당 지점까지의 최대 소요 시간(대중 교통 이용시)
     * 1. 각 지점으로 부터 가장 가까운 지하철역 찾기
     * 2. 지점 A로 부터의 가까운 역들과, 지점 B로 부터의 가까운 역들의 조합들 찾기
     * 3. 각 조합에 대해서, 모든 지하철역들을 주어진 시간내에 도달 가능한 공통의 지하철역들 찾기
     */
    @PostMapping("/multiple_points_time")
    @Operation(summary = "Fetch Transit Route from Multiple Point and time")
    public void fetchTransitTimeFromMultiplePointsAndTime(@RequestBody List<PointTravelTimeRequest> request){
        /*
        // GS타워, 롯데타워
        [
          {
            "x": "127.0376",
            "y": "37.5024",
            "time": 1800000
          },
          {
            "x": "127.102778",
            "y": "37.5125",
            "time": 1800000
          }
        ]
         */
        // 각 지점으로부터 가장 까가운 지하철역 찾기
        List<List<KakaoSearchByCategoryResponse>> nearestSubwaysPerRequest = request.stream()
                .map(r -> subwayService.findToNearestSubway(Point.of(r.getX(), r.getY())))
                .collect(Collectors.toList());

        routeService.fetchTransitTimeFromMultiplePointsAndTime(nearestSubwaysPerRequest);
    }
}
