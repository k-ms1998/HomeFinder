package com.project.homeFinder.dto.request;

import com.project.homeFinder.dto.Point;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PointTravelTimeRequest {

    private String x;
    private String y;
    private Long time;

    public static PointTravelTimeRequest of(String x, String y, Long time) {
        return new PointTravelTimeRequest(x, y, time);
    }
}
