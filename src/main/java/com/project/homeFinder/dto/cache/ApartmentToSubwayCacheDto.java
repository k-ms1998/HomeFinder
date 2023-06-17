package com.project.homeFinder.dto.cache;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ApartmentToSubwayCacheDto {

    private Long id;
    private Long apartmentId;
    private Long subwayId;
    private Long distance;
    private Long time;

    public static ApartmentToSubwayCacheDto of(Long id, Long apartmentId, Long subwayId, Long distance, Long time) {
        return new ApartmentToSubwayCacheDto(id, apartmentId, subwayId, distance, time);
    }

}
