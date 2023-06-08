package com.project.homeFinder.dto.response.raw.json;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class KakaoSearchByAddressResponseRaw {

    private LocalMeta meta;
    private List<ComplexAddress> documents;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class LocalMeta{
        private Integer total_count;
        private Integer pageable_count;
        private boolean is_end;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class ComplexAddress{
        private String address_name;
        private String address_type;
        private String x;
        private String y;
        private Address address;
        private RoadAddress road_address;

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        @ToString
        public static class Address{
            private String address_name;
            private String region_1depth_name;
            private String region_2depth_name;
            private String region_3depth_name;
            private String region_3depth_h_name;
            private String h_code;
            private String b_code;
            private String mountain_yn;
            private String main_address_no;
            private String sub_address_no;
            private String x;
            private String y;

        }

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        @ToString
        public static class RoadAddress{
            private String address_name;
            private String region_1depth_name;
            private String region_2depth_name;
            private String region_3depth_name;
            private String road_name;
            private String underground_yn;
            private String main_building_no;
            private String sub_building_no;
            private String building_name;
            private String zone_no;
            private String x;
            private String y;
        }
    }
}
