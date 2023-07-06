package com.project.homeFinder.domain;

import com.project.homeFinder.util.ServiceUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BusStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String stationId; // 정류장번호

    private String stationName; // 정류장명
    private String latitude; // 위도
    private String longitude; // 경도
    private String collectedDate; // 정보수집일시
    private String mobileCode; // 모바일단축코드
    private String cityCode; // 도시코드
    private String cityName; // 도시명
    private String adminCity; // 관리도시명

    public static BusStation of(String stationId, String stationName, String latitude, String longitude, String collectedDate, String mobileCode, String cityCode, String cityName, String adminCity) {
        return new BusStation(
                null,
                ServiceUtils.encodeString(stationId),
                ServiceUtils.encodeString(stationName),
                latitude,
                longitude,
                collectedDate,
                mobileCode,
                cityCode,
                ServiceUtils.encodeString(cityName),
                ServiceUtils.encodeString(adminCity)
        );
    }

}
