package com.project.homeFinder.service;

import com.project.homeFinder.domain.BusStation;
import com.project.homeFinder.repository.BusStationRepository;
import com.project.homeFinder.util.ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BusStationService {

    @Value("${myData.base.path.busstation}")
    private String BASE_PATH;


    private final BusStationRepository busStationRepository;

    @Transactional
    public List<BusStation> readFileAndSaveBusStationInfo() {
        final String filename = ServiceUtils.encodeString("2022년_전국버스정류장 위치정보_데이터.csv");
        if(invalidFilename(filename)){ // 파일이 '.csv' 로 끝나는지확인
            throw new RuntimeException("File does not end with '.csv'. Please check the filename");
        }

        final String file = BASE_PATH + filename;

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader);
            boolean skipFirst = false;

            List<BusStation> busStationList = new ArrayList<>();
            while (true) {
                String row = br.readLine();
                if (row == null) {
                    break;
                }
                if (!skipFirst) {
                    skipFirst = true;
                    continue;
                }

                String[] arr = row.split(",");
                String stationId = arr[0]; // 정류장번호
                String stationName = arr[1]; // 정류장명
                String latitude = arr[2]; // 위도
                String longitude = arr[3]; // 경도
                String collectedDate = arr[4]; // 정보수집일시
                String mobileCode = arr[5]; // 모바일단축코드
                String cityCode = arr[6]; // 도시코드
                String cityName = arr[7]; // 도시명
                String adminCity = arr[8]; // 관리도시명

                BusStation busStation = BusStation.of(stationId, stationName, latitude, longitude,
                        collectedDate, mobileCode, cityCode, cityName, adminCity);

                boolean isEmpty = busStationRepository.findByStationName(stationName).isEmpty();
                if(isEmpty){
                    busStationList.add(busStation);
                }
            }

            busStationRepository.saveAllAndFlush(busStationList);

            return busStationList;

        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found.");
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while reading file.");
        }

    }

    private boolean invalidFilename(String filename) {
        return !filename.endsWith(".csv");
    }
}
