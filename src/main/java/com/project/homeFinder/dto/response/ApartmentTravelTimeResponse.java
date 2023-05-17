package com.project.homeFinder.dto.response;

import com.project.homeFinder.dto.response.domain.ApartmentTravelTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApartmentTravelTimeResponse {

    private Long count;
    private List<ApartmentTravelTimeInfo> data;

    public static ApartmentTravelTimeResponse of(Long count, Map<String, List<ApartmentTravelTime>> data) {
        return new ApartmentTravelTimeResponse(
                count,
                data.keySet().stream()
                        .map(k -> ApartmentTravelTimeInfo.of(k, data.get(k)))
                        .collect(Collectors.toList()));
    }

    @Getter
    @AllArgsConstructor
    private static class ApartmentTravelTimeInfo{
        String dong;
        List<ApartmentTravelTime> apartmentTravelTime;

        public static ApartmentTravelTimeInfo of(String dong, List<ApartmentTravelTime> apartmentTravelTime) {
            return new ApartmentTravelTimeInfo(dong, apartmentTravelTime);
        }
    }
}
