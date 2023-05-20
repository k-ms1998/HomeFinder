package com.project.homeFinder.dto.response.domain;

import com.project.homeFinder.domain.Apartment;
import com.project.homeFinder.domain.Subway;
import com.project.homeFinder.dto.response.TotalTimeAndTransferCount;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ApartmentTravelTime {

    private Apartment apartment;
    private Map<Subway, TotalTimeAndTransferCount> totalTimeAndTransferCount;

    public static ApartmentTravelTime of(Apartment apartment, Map<Subway, TotalTimeAndTransferCount> totalTimeAndTransferCount) {
        return new ApartmentTravelTime(apartment, totalTimeAndTransferCount);
    }
}
