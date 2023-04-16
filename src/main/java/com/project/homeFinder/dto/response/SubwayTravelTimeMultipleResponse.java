package com.project.homeFinder.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SubwayTravelTimeMultipleResponse {

    private Long count;
    private List<TravelInfo> travelInfos;

    public static SubwayTravelTimeMultipleResponse of(Long count, List<TravelInfo> travelInfos) {
        return new SubwayTravelTimeMultipleResponse(count, travelInfos);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TravelInfo{
        private String destination;
        private List<StartInfo> startInfos;

        public static TravelInfo of(String destination, List<StartInfo> startInfos) {
            return new TravelInfo(destination, startInfos);
        }

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class StartInfo{
            private String name;
            private TotalTimeAndTransferCount totalTimeAndTransferCount;

            public static StartInfo of(String name, TotalTimeAndTransferCount totalTimeAndTransferCount) {
                return new StartInfo(name, totalTimeAndTransferCount);
            }
        }
    }

}
