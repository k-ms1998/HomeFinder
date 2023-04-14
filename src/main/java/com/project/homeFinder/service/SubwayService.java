package com.project.homeFinder.service;

import com.project.homeFinder.domain.Subway;
import com.project.homeFinder.domain.SubwayTravelTime;
import com.project.homeFinder.dto.Point;
import com.project.homeFinder.dto.enums.Area;
import com.project.homeFinder.dto.request.SingleDestinationRequest;
import com.project.homeFinder.dto.response.SingleDestinationResponse;
import com.project.homeFinder.dto.response.SubwayTravelTimeResponse;
import com.project.homeFinder.dto.response.TotalTimeAndTransferCount;
import com.project.homeFinder.repository.SubwayRepository;
import com.project.homeFinder.repository.SubwayTravelTimeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubwayService {

    @Value("${myData.base.path.subway}")
    private String BASE_PATH;

    private final RouteService routeService;

    private final SubwayRepository subwayRepository;
    private final SubwayTravelTimeRepository subwayTravelTimeRepository;

    @Transactional
    public Long readFileAndSave(String filename, String order, String area) {
        if(invalidFilename(filename)){ // 파일이 '.csv' 로 끝나는지확인
            throw new RuntimeException("File does not end with '.csv'. Please check the filename");
        }
        if(invalidOrder(order)){ // order 의 크기가 4이고, 중복되는 값이 없어야함
            throw new RuntimeException("Invalid order.");
        }
        if(invalidArea(area)){
            throw new RuntimeException("Invalid area.");
        }

        String file = BASE_PATH + filename;
        /*
        호선, 지하철역 명, 경도, 위도 순서
        ex: seoul_subway_1_8_2023_04.csv 는 Column 순서가 연번, 호선, 고유역번호, 역명, 위도, 경도, 작성일자 => orderArr = {1, 3, 5, 4}
            seoul_subway_9_2023_04.csv 는 Column 순서가 철도운영기관명, 선명, 역명, 경도, 위도 => orderArr = {1, 2, 3, 4}
         */
        String[] orderArr = order.split("");
        Long count = 0L;
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader);
            boolean skipFirst = false;
            while (true) {
                String row = br.readLine();
                if (row == null) {
                    break;
                }
                if (!skipFirst) { // 첫번째 줄은 column 명들이므로 건너뛰기
                    skipFirst = true;
                    continue;
                }
                String[] arr = row.split(",");
                String line = encodeString(area + "_" + arr[Integer.parseInt(orderArr[0])]); // 서울 1호선 -> seoul_1
                String name = encodeString(arr[Integer.parseInt(orderArr[1])]);
                String x = arr[Integer.parseInt(orderArr[2])];
                String y = arr[Integer.parseInt(orderArr[3])];
                if(save(line, name, x, y)){
                    count++;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return count;
    }

        public boolean save(String line, String name, String x, String y) {
        // 유니크한 (호선 - 지하철역 이름)이 이미 있는지 확인
        if (subwayRepository.findByLineAndName(line, name).isEmpty()) {
            subwayRepository.save(Subway.of(line, name, x, y));
            return true;
        }

        return false;
    }

    @Transactional
    public SubwayTravelTimeResponse findTimeFromSubwayToSubwayByKeyword(String keywordA, String keywordB) {
        String encodedKeywordA = encodeString(keywordA);
        String encodedKeywordB = encodeString(keywordB);

        Subway subwayA = findSubwayByKeyword(encodedKeywordA).stream()
                .findFirst().orElseThrow(() -> new RuntimeException("Invalid Keyword. Check First keyword."));

        Subway subwayB = findSubwayByKeyword(encodedKeywordB).stream()
                .findFirst().orElseThrow(() -> new RuntimeException("Invalid Keyword. Check Second keyword."));

        SubwayTravelTime subwayTravelTime = subwayTravelTimeRepository.findBySubwayToSubway(subwayA, subwayB)
                .orElseGet(() -> {
                    log.info("Subway Travel Time Not Found: {} -> {}", subwayA.getName(), subwayB.getName());
                    SingleDestinationResponse response = routeService.toSingleDestination(SingleDestinationRequest.of(
                            Point.of(subwayA.getX(), subwayA.getY()),
                            Point.of(subwayB.getX(), subwayB.getY())
                    ));
                    log.info("response: {}", response);
                    TotalTimeAndTransferCount totalTimeAndTransferCount = response.getTotalTimeAndTransferCount();

                    return SubwayTravelTime.of(subwayA, subwayB, totalTimeAndTransferCount.getTotalTime(), totalTimeAndTransferCount.getTransferCount());
                });

        return SubwayTravelTimeResponse.from(subwayTravelTime);
    }

    private List<Subway> findSubwayByKeyword(String keyword){

        return subwayRepository.findAllByName(keyword);
    }

    private boolean invalidFilename(String filename) {
        return !filename.endsWith(".csv");
    }

    private boolean invalidOrder(String order) {
        if (order.length() != 4) {
            return true;
        }

        Set<String> columnIdx = new HashSet<>();
        for(int i = 0; i < 4; i++){
            columnIdx.add(String.valueOf(order.charAt(i)));
        }

        return columnIdx.size() != 4;
    }

    private boolean invalidArea(String area) {
        return Arrays.stream(Area.values()).noneMatch(a -> a.name().equals(area));
    }

    private String encodeString(String input) {

        return new String(input.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
    }
}
