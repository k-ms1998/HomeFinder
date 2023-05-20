package com.project.homeFinder.dto.response;

import com.project.homeFinder.domain.Subway;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SubwayTravelTimeMultipleEntityResponse {

    private Long count;
    private List<TravelInfo> travelInfos;

    public static SubwayTravelTimeMultipleEntityResponse of(Long count, List<TravelInfo> travelInfos) {
        return new SubwayTravelTimeMultipleEntityResponse(count, travelInfos);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TravelInfo{
        private Subway destination;
        private List<StartInfo> startInfos;

        public static TravelInfo of(Subway destination, List<StartInfo> startInfos) {
            return new TravelInfo(destination, startInfos);
        }

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class StartInfo{
            private Subway start;
            private TotalTimeAndTransferCount totalTimeAndTransferCount;

            public static StartInfo of(Subway start, TotalTimeAndTransferCount totalTimeAndTransferCount) {
                return new StartInfo(start, totalTimeAndTransferCount);
            }
        }
    }

}
