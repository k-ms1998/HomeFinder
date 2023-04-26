package com.project.homeFinder.dto.request;

import com.project.homeFinder.dto.response.KakaoSearchByCategoryResponse;
import com.project.homeFinder.util.ServiceUtils;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SubwayTravelTimeRequest {

    private String name;
    private String x;
    private String y;
    private Long time;

    public SubwayTravelTimeRequest(String name, Long time) {
        this.name = name;
        this.time = time;
    }

    public static SubwayTravelTimeRequest of(String name, Long time) {
        return SubwayTravelTimeRequest.of(name, "", "", time);
    }

    public static SubwayTravelTimeRequest of(String name, String x, String y, Long time) {
        return new SubwayTravelTimeRequest(ServiceUtils.encodeString(name), x, y, time);
    }

    public static SubwayTravelTimeRequest fromKakaoSearchByCategoryResponse(KakaoSearchByCategoryResponse response, Long time) {
        return new SubwayTravelTimeRequest(ServiceUtils.encodeString(response.getName()), response.getX(), response.getY(), time);
    }

}
