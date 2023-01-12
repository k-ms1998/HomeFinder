package com.project.homeFinder.dto.response.raw;

import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * https://apis.openapi.sk.com/transit/routes/sub 요청시 받은 Response를 그대로 담을 객체
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TMapTransitRoutesSubRawResponse {

    private MetaData metaData;

    public static TMapTransitRoutesSubRawResponse from(Map<String, Object> attributes) {
        return new TMapTransitRoutesSubRawResponse(
                MetaData.from((Map<String, Object>) attributes.get("metaData"))
        );
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MetaData {
        private RequestParameters requestParameters;
        private Plan plan;

        public static MetaData from(Map<String, Object> attributes) {
            return new MetaData(
                    RequestParameters.from((Map<String, Object>) attributes.get("requestParameters")),
                    Plan.from((Map<String, Object>) attributes.get("plan"))
            );
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class RequestParameters {
            private String endY;
            private String endX;
            private String startY;
            private String startX;
            private String reqDttm;

            public static RequestParameters from(Map<String, Object> attributes) {
                return new RequestParameters(
                        String.valueOf(attributes.get("endY")),
                        String.valueOf(attributes.get("endX")),
                        String.valueOf(attributes.get("startY")),
                        String.valueOf(attributes.get("startX")),
                        String.valueOf(attributes.get("reqDttm"))
                );
            }
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Plan {
            private Itineraries[] itineraries;

            public static Plan from(Map<String, Object> attributes) {
                List<Map<String, Object>> rawItineraries = (List<Map<String, Object>>) attributes.get("itineraries");
                Itineraries[] finalItineraries = new Itineraries[rawItineraries.size()];
                int idx = 0;
                for (Map<String, Object> rawItinerary : rawItineraries) {
                    finalItineraries[idx] = Itineraries.from(rawItinerary);
                    ++idx;
                }

                return new Plan(finalItineraries);
            }

            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            public static class Itineraries {
                private Fare fare;
                private Long walkDistance;
                private Long totalTime;
                private Long walkTime;
                private Long transferCount;
                private Long totalDistance;

                public static Itineraries from(Map<String, Object> attributes) {
                    return new Itineraries(
                            Fare.from((Map<String, Object>) attributes.get("fare")),
                            Long.valueOf(String.valueOf(attributes.get("walkDistance"))),
                            Long.valueOf(String.valueOf(attributes.get("totalTime"))),
                            Long.valueOf(String.valueOf(attributes.get("walkTime"))),
                            Long.valueOf(String.valueOf(attributes.get("transferCount"))),
                            Long.valueOf(String.valueOf(attributes.get("totalDistance")))
                    );
                }

                @Data
                @NoArgsConstructor
                @AllArgsConstructor
                public static class Fare {
                    private Regular regular;

                    public static Fare from(Map<String, Object> attributes) {
                        return new Fare(
                                Regular.from((Map<String, Object>) attributes.get("regular"))
                        );
                    }

                    @Data
                    @NoArgsConstructor
                    @AllArgsConstructor
                    public static class Regular {
                        private Long totalFare;
                        private Currency currency;

                        public static Regular from(Map<String, Object> attributes) {
                            return new Regular(
                                    Long.valueOf(String.valueOf(attributes.get("totalFare"))),
                                    Currency.from((Map<String, Object>) attributes.get("currency"))
                            );
                        }

                        @Data
                        @NoArgsConstructor
                        @AllArgsConstructor
                        public static class Currency {
                            private String symbol;
                            private String currency;
                            private String currencyCode;

                            public static Currency from(Map<String, Object> attributes) {
                                return new Currency(
                                        String.valueOf(attributes.get("symbol")),
                                        String.valueOf(attributes.get("currency")),
                                        String.valueOf(attributes.get("currencyCode"))
                                );
                            }
                        }
                    }

                }

            }
        }
    }
}
