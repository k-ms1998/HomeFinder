package com.project.homeFinder.service;

import com.project.homeFinder.domain.Subway;
import com.project.homeFinder.dto.Point;
import com.project.homeFinder.dto.enums.Area;
import com.project.homeFinder.dto.response.*;
import com.project.homeFinder.repository.SubwayRepository;
import com.project.homeFinder.service.api.KakaoApi;
import com.project.homeFinder.util.ServiceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubwayService {

    @Value("${myData.base.path.subway}")
    private String BASE_PATH;

    private final KakaoApi kakaoApi;

    private final SubwayRepository subwayRepository;

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
                String line = ServiceUtils.encodeString(area + "_" + arr[Integer.parseInt(orderArr[0])]); // 서울 1호선 -> seoul_1
                String name = ServiceUtils.encodeString(arr[Integer.parseInt(orderArr[1])]);
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

    public List<KakaoSearchByCategoryResponse> findToNearestSubway(Point request) {
        List<KakaoSearchByCategoryResponse> responseRaw = kakaoApi.findToNearestSubway(request);

        List<KakaoSearchByCategoryResponse> response = new ArrayList<>();
        Set<String> subwayNamesSet = new HashSet<>();
        for (KakaoSearchByCategoryResponse res : responseRaw) {
            // 지하철역 이름이 'OO역 O호선' 으로 반횐됨 -> 0호선 부분은 버리고 OO역만 확인
            String name = res.getName().split(" ")[0];
            if(!subwayNamesSet.contains(name)){
                response.add(KakaoSearchByCategoryResponse.of(name, res.getX(), res.getY(), res.getDistance())); // 0호선 바린 값으로 반환
                subwayNamesSet.add(name);
            }
        }

        return response;
    }

    public List<Subway> findSubwayByKeyword(String keyword){

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

}
