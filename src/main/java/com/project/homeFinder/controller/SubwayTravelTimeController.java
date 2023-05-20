package com.project.homeFinder.controller;

import com.project.homeFinder.domain.Subway;
import com.project.homeFinder.domain.SubwayTravelTime;
import com.project.homeFinder.dto.request.PointTravelTimeRequest;
import com.project.homeFinder.dto.request.SubwayTravelTimeRequest;
import com.project.homeFinder.dto.response.ApartmentTravelTimeResponse;
import com.project.homeFinder.dto.response.SubwayTravelTimeMultipleEntityResponse;
import com.project.homeFinder.dto.response.SubwayTravelTimeMultipleResponse;
import com.project.homeFinder.dto.response.SubwayTravelTimeResponse;
import com.project.homeFinder.dto.response.domain.ApartmentTravelTime;
import com.project.homeFinder.service.SubwayTravelTimeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subway/time")
public class SubwayTravelTimeController {

    private final SubwayTravelTimeService subwayTravelTimeService;

    @GetMapping(value = "/by")
    @Operation(summary = "All subway stations within given time")
    public ResponseEntity<List<SubwayTravelTimeResponse>> findSubwaysByTime(@RequestParam String name, @RequestParam String time) {
        return ResponseEntity.ok(subwayTravelTimeService.findSubwaysByTime(name, time));
    }

    @PostMapping(value = "/by/multiple")
    @Operation(summary = "All subway stations within given time")
    public ResponseEntity<SubwayTravelTimeMultipleResponse> findSubwaysByTimeMultiple(@RequestBody List<SubwayTravelTimeRequest> requests) {
        return ResponseEntity.ok(subwayTravelTimeService.findSubwaysByTimeMultiple(requests));
    }

    @GetMapping(value = "/keyword")
    @Operation(summary = "Find time to travel from subway to subway")
    public ResponseEntity<SubwayTravelTimeResponse> findTimeFromSubwayToSubwayByKeyword(@RequestParam String keywordA, @RequestParam String keywordB){
        return ResponseEntity.ok(subwayTravelTimeService.findTimeFromSubwayToSubwayByKeyword(keywordA, keywordB));
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
    public ResponseEntity<SubwayTravelTimeMultipleResponse> fetchTransitTimeFromMultiplePointsAndTime(@RequestBody List<PointTravelTimeRequest> request){
        /*
        // GS타워, 롯데타워
        [
          {
            "x": "127.0376",
            "y": "37.5024",
            "time": 2400000
          },
          {
            "x": "127.102778",
            "y": "37.5125",
            "time": 2100000
          }
        ]
         */
        return ResponseEntity.ok(subwayTravelTimeService.findTransitTimeFromMultiplePointsAndTime(request));
    }

    /**
     *
     * @param request
     * Point: 시작 지점의 좌표
     * Time: 해당 지점까지의 최대 소요 시간(대중 교통 이용시)
     * 1. 각 지점으로 부터 가장 가까운 지하철역 찾기
     * 2. 지점 A로 부터의 가까운 역들과, 지점 B로 부터의 가까운 역들의 조합들 찾기
     * 3. 각 조합에 대해서, 모든 지하철역들을 주어진 시간내에 도달 가능한 공통의 지하철역들 찾기
     * 4. 공통의 지하철역들 중에서 가까운 아파트들 찾기
     * 5. 각 아파트들, 동 별로 묶어서 반환
     *  -> 예: 잠실동에 존재하면서 조건들을 만족하는 아파트들이 많으면, (K, V) -> ('잠실돟', {아파트 A, 아파트 B, 아파트 C...}) 형태의 data 반환
     */
    @PostMapping("/multiple_points_time/apt")
    @Operation(summary = "Fetch Transit Route from Multiple Point and time")
    public ResponseEntity<ApartmentTravelTimeResponse> fetchTransitTimeFromMultiplePointsAndTimeApt(@RequestBody List<PointTravelTimeRequest> request){

        return ResponseEntity.ok(subwayTravelTimeService.findTransitTimeFromMultiplePointsAndTimeApt(request));
    }
}
