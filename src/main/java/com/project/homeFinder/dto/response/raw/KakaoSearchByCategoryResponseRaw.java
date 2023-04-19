package com.project.homeFinder.dto.response.raw;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class KakaoSearchByCategoryResponseRaw {

    private PlaceMeta meta;
    private List<Place> documents;

    public static KakaoSearchByCategoryResponseRaw of(PlaceMeta meta, List<Place> documents) {
        return new KakaoSearchByCategoryResponseRaw(meta, documents);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class PlaceMeta {
        private int total_count;
        private int pageable_count;
        private boolean is_end;
        private RegionInfo same_name;

        public static PlaceMeta of(int totalCount, int pageableCount, boolean isEnd, RegionInfo sameName) {
            return new PlaceMeta(totalCount, pageableCount, isEnd, sameName);
        }

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        @ToString
        public static class RegionInfo {
            private List<String> region;
            private String keyword;
            private String selected_region;

            public static RegionInfo of(List<String> region, String keyword, String selectedRegion) {
                return new RegionInfo(region, keyword, selectedRegion);
            }

        }
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Place {
        private String id;
        private String place_name;
        private String category_name;
        private String category_group_code;
        private String category_group_name;
        private String phone;
        private String address_name;
        private String roadAddress_name;
        private String x;
        private String y;
        private String place_url;
        private String distance;

        public static Place of(String id, String placeName, String categoryName, String categoryGroupCode, String categoryGroupName,
                                       String phone, String addressName, String roadAddressName, String x, String y, String placeUrl, String distance) {
            return new Place(id, placeName, categoryName, categoryGroupCode, categoryGroupName,
                    phone, addressName, roadAddressName, x, y, placeUrl, distance);
        }

    }

}
