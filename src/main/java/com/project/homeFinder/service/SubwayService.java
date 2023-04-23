package com.project.homeFinder.service;

import com.project.homeFinder.domain.Subway;
import com.project.homeFinder.domain.SubwayTravelTime;
import com.project.homeFinder.dto.Point;
import com.project.homeFinder.dto.enums.Area;
import com.project.homeFinder.dto.request.SingleDestinationRequest;
import com.project.homeFinder.dto.request.SubwayTravelTimeRequest;
import com.project.homeFinder.dto.response.*;
import com.project.homeFinder.dto.response.SubwayTravelTimeMultipleResponse.TravelInfo;
import com.project.homeFinder.dto.response.SubwayTravelTimeMultipleResponse.TravelInfo.StartInfo;
import com.project.homeFinder.dto.response.raw.KakaoSearchByCategoryResponseRaw;
import com.project.homeFinder.repository.SubwayRepository;
import com.project.homeFinder.repository.SubwayTravelTimeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubwayService {

    @Value("${myData.base.path.subway}")
    private String BASE_PATH;

    @Value("${kakao.api.key}")
    private String KAKAO_API_KEY;

    private static final int MAX_DISTANCE = 1000;

    private final RouteService routeService;

    private final SubwayRepository subwayRepository;
    private final SubwayTravelTimeRepository subwayTravelTimeRepository;

    private final WebClient webClient;

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

        subwayTravelTimeRepository.save(subwayTravelTime);
        return SubwayTravelTimeResponse.from(subwayTravelTime);
    }

    public List<SubwayTravelTimeResponse> findSubwaysByTime(String name, String time) {
        List<Subway> subways = findSubwayByKeyword(encodeString(name));
        if(isResultListEmpty(subways)){
            return null;
        }

        Subway subway = subways.get(0);
        return subwayTravelTimeRepository.findByTimeFromSubway(subway, Long.valueOf(time)).stream()
                .map(SubwayTravelTimeResponse::from)
                .collect(Collectors.toList());

    }

    // 각 출발지점으로 부터, 주어진 시간이내의 모든 지하철역들 찾기
    public SubwayTravelTimeMultipleResponse findSubwaysByTimeMultiple(List<SubwayTravelTimeRequest> requests) {
        final int targetSize = requests.size();

        Set<String> targets = new HashSet<>();
        List<SubwayTravelTimeResponse> tmpValue = new ArrayList<>();
        for (SubwayTravelTimeRequest request : requests) {
            String start = encodeString(request.getName());
            Long time = request.getTime();
            targets.add(start);

            List<Subway> subways = findSubwayByKeyword(encodeString(start));
            if(isResultListEmpty(subways)){
                log.info("Subway {} does not exist.", start);
                continue;
            }
            
            Subway subway = subways.get(0);
            List<SubwayTravelTimeResponse> byTimeFromSubway = subwayTravelTimeRepository.findByTimeFromSubway(subway, Long.valueOf(time)).stream()
                    .map(SubwayTravelTimeResponse::from)
                    .collect(Collectors.toList());// request에서 time내에 있는 모든 지하철역들
            tmpValue.addAll(byTimeFromSubway);
        }

        Map<String, Set<String>> tmpAddedSet = new HashMap<>();
        Map<String, List<SubwayTravelTimeResponse>> tmpValueMap = new HashMap<>();
        for (SubwayTravelTimeResponse stt : tmpValue) {
            String start = stt.getStart();
            String destination = stt.getDestination();

            List<SubwayTravelTimeResponse> sttStart = tmpValueMap.getOrDefault(start, new ArrayList<>());
            List<SubwayTravelTimeResponse> sttDestination = tmpValueMap.getOrDefault(destination, new ArrayList<>());
            if(targets.contains(start)){
                Set<String> addedSet = tmpAddedSet.getOrDefault(destination, new HashSet<>());
                if(!addedSet.contains(start)){
                    sttDestination.add(stt);
                    tmpValueMap.put(destination, sttDestination);
                    addedSet.add(start);
                    tmpAddedSet.put(destination, addedSet);
                }
            }
            if(targets.contains(destination)){
                Set<String> addedSet = tmpAddedSet.getOrDefault(start, new HashSet<>());
                if(!addedSet.contains(destination)){
                    sttStart.add(stt);
                    tmpValueMap.put(start, sttStart);
                    addedSet.add(destination);
                    tmpAddedSet.put(start, addedSet);
                }
            }
        }
//        for (String s : tmpValueMap.keySet()) {
//            log.info("destination: {}, tmpValueMap: {}", encodeString(s), tmpValueMap.get(s));
//        }

        List<TravelInfo> result = tmpValueMap.keySet().stream()
                .filter(k -> targets.contains(k) ? (tmpValueMap.get(k).size() >= targetSize - 1) : (tmpValueMap.get(k).size() >= targetSize))
                .map(k -> {
                    List<SubwayTravelTimeResponse> subwayTravelTimeResponses = tmpValueMap.get(k);
                    return TravelInfo.of(k, subwayTravelTimeResponses.stream()
                            .map(stt -> StartInfo.of(stt.getStart() == k ? stt.getDestination() : stt.getStart(),
                                    TotalTimeAndTransferCount.of(stt.getTotalTime(), stt.getTransferCount())))
                            .collect(Collectors.toList()));
                }).collect(Collectors.toList());


        return SubwayTravelTimeMultipleResponse.of(Long.valueOf(result.size()), result);
    }

    public List<KakaoSearchByCategoryResponse> findToNearestSubway(Point request) {
        final String uri =
                String.format("https://dapi.kakao.com/v2/local/search/category.json?category_group_code=SW8&page=1&size=5&sort=distance&x=%s&y=%s&radius=1000", request.getX(), request.getY());

        List<KakaoSearchByCategoryResponse> responseRaw = webClient.get()
                .uri(uri)
                .header("Authorization", "KakaoAK " + KAKAO_API_KEY)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(KakaoSearchByCategoryResponseRaw.class)
                .map(res -> res.getDocuments())
                .map(KakaoSearchByCategoryResponse::fromDocumentList)
                .single().blockOptional()
                .orElseThrow(() -> new RuntimeException("No Results."));

        log.info("KakaoSearchByCategoryResponseRaw: {}", responseRaw);

        List<KakaoSearchByCategoryResponse> response = new ArrayList<>();
        Set<String> subwayNamesSet = new HashSet<>();
        for (KakaoSearchByCategoryResponse res : responseRaw) {
            String name = res.getName();
            if(!subwayNamesSet.contains(name)){
                response.add(res);
            }
        }

        return response;
    }

    private List<Subway> findSubwayByKeyword(String keyword){

        return subwayRepository.findAllByName(keyword);
    }

    private boolean isResultListEmpty(List<?> result) {
        return result.size() == 0;
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
