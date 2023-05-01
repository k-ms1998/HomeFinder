package com.project.homeFinder.service;

import com.project.homeFinder.domain.Subway;
import com.project.homeFinder.domain.SubwayTravelTime;
import com.project.homeFinder.dto.Point;
import com.project.homeFinder.dto.request.PointTravelTimeRequest;
import com.project.homeFinder.dto.request.SingleDestinationRequest;
import com.project.homeFinder.dto.request.SubwayTravelTimeRequest;
import com.project.homeFinder.dto.response.SingleDestinationResponse;
import com.project.homeFinder.dto.response.SubwayTravelTimeMultipleResponse;
import com.project.homeFinder.dto.response.SubwayTravelTimeResponse;
import com.project.homeFinder.dto.response.TotalTimeAndTransferCount;
import com.project.homeFinder.repository.SubwayTravelTimeRepository;
import com.project.homeFinder.service.api.KakaoApi;
import com.project.homeFinder.util.ServiceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubwayTravelTimeService {

    private final RouteService routeService;
    private final SubwayService subwayService;
    private final SubwayTravelTimeRepository subwayTravelTimeRepository;
    private final KakaoApi kakaoApi;

    // 각 출발지점으로 부터, 주어진 시간이내의 모든 지하철역들 찾기
    public SubwayTravelTimeMultipleResponse findSubwaysByTimeMultiple(List<SubwayTravelTimeRequest> requests) {
        final int targetSize = requests.size();

        Set<String> targets = new HashSet<>();
        List<SubwayTravelTimeResponse> tmpValue = new ArrayList<>();
        for (SubwayTravelTimeRequest request : requests) {
            String start = ServiceUtils.encodeString(request.getName());
            Long time = request.getTime();
            targets.add(start);

            List<Subway> subways = subwayService.findSubwayByKeyword(ServiceUtils.checkAndRemoveSubwayNameSuffixAndEncode(start));
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

        List<SubwayTravelTimeMultipleResponse.TravelInfo> result = tmpValueMap.keySet().stream()
                .filter(k -> targets.contains(k) ? (tmpValueMap.get(k).size() >= targetSize - 1) : (tmpValueMap.get(k).size() >= targetSize))
                .map(k -> {
                    List<SubwayTravelTimeResponse> subwayTravelTimeResponses = tmpValueMap.get(k);
                    return SubwayTravelTimeMultipleResponse.TravelInfo.of(k, subwayTravelTimeResponses.stream()
                            .map(stt -> SubwayTravelTimeMultipleResponse.TravelInfo.StartInfo.of(stt.getStart() == k ? stt.getDestination() : stt.getStart(),
                                    TotalTimeAndTransferCount.of(stt.getTotalTime(), stt.getTransferCount())))
                            .collect(Collectors.toList()));
                }).collect(Collectors.toList());

        return SubwayTravelTimeMultipleResponse.of(Long.valueOf(result.size()), result);
    }

    public SubwayTravelTimeMultipleResponse findTransitTimeFromMultiplePointsAndTime(List<PointTravelTimeRequest> pointTravelTimeRequest) {
        // 각 지점으로부터 가장 까가운 지하철역 찾기
        List<List<SubwayTravelTimeRequest>> request = pointTravelTimeRequest.stream()
                .map(r -> kakaoApi.findToNearestSubway(Point.of(r.getX(), r.getY())).stream()
                        .map(ksbcr -> SubwayTravelTimeRequest.fromKakaoSearchByCategoryResponse(ksbcr, r.getTime()))
                        .collect(Collectors.toList())
                )
                .collect(Collectors.toList());

        //각 지점으로부터 가장 가까운 지하철역들의 모든 조합들 구하기
        int destinationCount = request.size();

        if(destinationCount >= 5){ // 너무 많은 도착지점들을 입력할 경우, 서버 과부화가 일어날 수 있기 때문에 제한
            return null;
        }
        int nearestSubwayCount = destinationCount <= 2 ? 2 : 1; // 입력된 지점들에 따라서, 가까운 지하철역들의 개수 제한

        List<List<SubwayTravelTimeRequest>> subwayCombinations = new ArrayList<>();
        findSubwayCombinations(0, destinationCount, nearestSubwayCount, request, new ArrayList<>(), subwayCombinations);
        log.info("subwayCombinations: {}", subwayCombinations);

        List<SubwayTravelTimeMultipleResponse> result = subwayCombinations.stream()
                .map(sc1 -> sc1.stream()
                        .map(sttr -> SubwayTravelTimeRequest.of(ServiceUtils.checkAndRemoveSubwayNameSuffix(sttr.getName()), sttr.getTime()))
                        .collect(Collectors.toList())
                )
                .map(sc2 -> findSubwaysByTimeMultiple(sc2))
                .collect(Collectors.toList());

        Map<String, Set<String>> sttmrSet = new HashMap<>();
        Map<String, List<SubwayTravelTimeMultipleResponse.TravelInfo.StartInfo>> sttmrMap = new HashMap<>();
        for (SubwayTravelTimeMultipleResponse subwayTravelTimeMultipleResponse : result) {
            subwayTravelTimeMultipleResponse.getTravelInfos().forEach(sttmr -> {
                String destination = sttmr.getDestination();
                List<SubwayTravelTimeMultipleResponse.TravelInfo.StartInfo> startInfos = sttmr.getStartInfos();
                List<SubwayTravelTimeMultipleResponse.TravelInfo.StartInfo> value = sttmrMap.getOrDefault(destination, new ArrayList<>());
                Set<String> added = sttmrSet.getOrDefault(destination, new HashSet<>());

                // 중복되는 StartInfo 들 제거
                value.addAll(startInfos.stream()
                        .filter(s -> {
                            String name = s.getName();
                            if(!added.contains(name)){
                                added.add(name);
                                return true;
                            }

                            return false;
                        })
                        .collect(Collectors.toList())
                );
                sttmrMap.put(destination, value);
                sttmrSet.put(destination, added);
            });
        }

        List<SubwayTravelTimeMultipleResponse.TravelInfo> travelInfo = sttmrMap.keySet().stream()
                .map(k -> new SubwayTravelTimeMultipleResponse.TravelInfo(k, sttmrMap.get(k)))
                .collect(Collectors.toList());


        return SubwayTravelTimeMultipleResponse.of(Long.valueOf(travelInfo.size()), travelInfo);
    }


    public List<SubwayTravelTimeResponse> findSubwaysByTime(String name, String time) {
        List<Subway> subways = subwayService.findSubwayByKeyword(ServiceUtils.checkAndRemoveSubwayNameSuffixAndEncode(name));
        if(isResultListEmpty(subways)){
            return null;
        }

        Subway subway = subways.get(0);
        return subwayTravelTimeRepository.findByTimeFromSubway(subway, Long.valueOf(time)).stream()
                .map(SubwayTravelTimeResponse::from)
                .collect(Collectors.toList());

    }

    @Transactional
    public SubwayTravelTimeResponse findTimeFromSubwayToSubwayByKeyword(String keywordA, String keywordB) {
        String encodedKeywordA = ServiceUtils.checkAndRemoveSubwayNameSuffixAndEncode(keywordA);
        String encodedKeywordB = ServiceUtils.checkAndRemoveSubwayNameSuffixAndEncode(keywordB);

        Subway subwayA = subwayService.findSubwayByKeyword(encodedKeywordA).stream()
                .findFirst().orElseThrow(() -> new RuntimeException("Invalid Keyword. Check First keyword."));

        Subway subwayB = subwayService.findSubwayByKeyword(encodedKeywordB).stream()
                .findFirst().orElseThrow(() -> new RuntimeException("Invalid Keyword. Check Second keyword."));

        SubwayTravelTime subwayTravelTime = subwayTravelTimeRepository.findBySubwayToSubway(subwayA, subwayB)
                .orElseGet(() -> {
                    log.info("Subway Travel Time Not Found: {} -> {}", subwayA.getName(), subwayB.getName());
                    SingleDestinationResponse response = routeService.toSingleDestination(SingleDestinationRequest.of(
                            Point.of(subwayA.getX(), subwayA.getY()),
                            Point.of(subwayB.getX(), subwayB.getY())
                    ));

                    TotalTimeAndTransferCount totalTimeAndTransferCount = response.getTotalTimeAndTransferCount();

                    return SubwayTravelTime.of(subwayA, subwayB, totalTimeAndTransferCount.getTotalTime(), totalTimeAndTransferCount.getTransferCount());
                });

        subwayTravelTimeRepository.save(subwayTravelTime);
        return SubwayTravelTimeResponse.from(subwayTravelTime);
    }


    private boolean isResultListEmpty(List<?> result) {
        return result.size() == 0;
    }

    private void findSubwayCombinations(int depth, int targetDepth, int nearestSubwayCount, List<List<SubwayTravelTimeRequest>> request,
                                        List<SubwayTravelTimeRequest> combination, List<List<SubwayTravelTimeRequest>> subwayCombinations) {
        if(depth >= targetDepth){
            subwayCombinations.add(combination);
            return;
        }
        List<SubwayTravelTimeRequest> nearestSubways = request.get(depth);
        int size = nearestSubways.size() < nearestSubwayCount ? nearestSubways.size() : nearestSubwayCount;
        for(int i = 0; i < size; i++){
            findSubwayCombinations(depth + 1, targetDepth, nearestSubwayCount, request, copyCombination(combination, nearestSubways.get(i)), subwayCombinations);
        }
    }

    private List<SubwayTravelTimeRequest> copyCombination(List<SubwayTravelTimeRequest> combination, SubwayTravelTimeRequest next) {
        List<SubwayTravelTimeRequest> list = new ArrayList<>();
        for (SubwayTravelTimeRequest sttr : combination) {
            list.add(SubwayTravelTimeRequest.of(sttr.getName(), sttr.getX(), sttr.getY(), sttr.getTime()));
        }
        list.add(SubwayTravelTimeRequest.of(next.getName(), next.getX(), next.getY(), next.getTime()));

        return list;
    }
}
