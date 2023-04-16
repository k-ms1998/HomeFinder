package com.project.homeFinder.controller;

import com.project.homeFinder.dto.request.SubwayTravelTimeRequest;
import com.project.homeFinder.dto.response.SubwayTravelTimeMultipleResponse;
import com.project.homeFinder.dto.response.SubwayTravelTimeResponse;
import com.project.homeFinder.service.SubwayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * https://www.data.go.kr/data/15099316/fileData.do?recommendDataYn=Y -> 서울 1~8호선: 지하철역 데이터 -> filename = seoul_subway_1_8_2023_04.csv, order = 1354, area = SEOUL
 * https://www.data.go.kr/data/15041335/fileData.do -> 서울 9호선: 지하철역 데이터 -> filename = seoul_subway_9_2023_04.csv, order = 1234, area = SEOUL
 */
@RestController
@RequestMapping("/subway")
@RequiredArgsConstructor
@Tag(name = "Subway", description = "Subway API Documentation")
public class SubwayController {

    private final SubwayService subwayService;

    @GetMapping("/file/add")
    @Operation(summary = "Read subway info and add to database if does not exist.")
    public ResponseEntity<Long> readAndAddCsv(@RequestParam String filename, @RequestParam String order, @RequestParam String area) {
        /*
        filename = 파일 명
        order = (line(호선), name(역명), x(경도), y(위도)) 순서
         */
        return ResponseEntity.status(HttpStatus.OK)
                .body(subwayService.readFileAndSave(filename, order, area));
    }

    @GetMapping(value = "/time/keyword")
    @Operation(summary = "Find time to travel from subway to subway")
    public ResponseEntity<SubwayTravelTimeResponse> findTimeFromSubwayToSubwayByKeyword(@RequestParam String keywordA, @RequestParam String keywordB){
        return ResponseEntity.ok(subwayService.findTimeFromSubwayToSubwayByKeyword(keywordA, keywordB));
    }

    @GetMapping(value = "/by/time")
    @Operation(summary = "All subway stations within given time")
    public ResponseEntity<List<SubwayTravelTimeResponse>> findSubwaysByTime(@RequestParam String name, @RequestParam String time) {
        return ResponseEntity.ok(subwayService.findSubwaysByTime(name, time));
    }

    @PostMapping(value = "/by/time/multiple")
    @Operation(summary = "All subway stations within given time")
    public ResponseEntity<SubwayTravelTimeMultipleResponse> findSubwaysByTimeMultiple(@RequestBody List<SubwayTravelTimeRequest> requests) {
        return ResponseEntity.ok(subwayService.findSubwaysByTimeMultiple(requests));
    }


}
