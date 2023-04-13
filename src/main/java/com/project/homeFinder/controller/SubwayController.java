package com.project.homeFinder.controller;

import com.project.homeFinder.dto.enums.Area;
import com.project.homeFinder.service.SubwayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


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

}
