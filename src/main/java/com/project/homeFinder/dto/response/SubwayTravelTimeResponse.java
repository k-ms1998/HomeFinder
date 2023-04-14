package com.project.homeFinder.dto.response;

import com.project.homeFinder.domain.SubwayTravelTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubwayTravelTimeResponse {

    private String start;
    private String destination;
    private Long totalTime;
    private Long transferCount;

    public static SubwayTravelTimeResponse of(String start, String destination, Long totalTime, Long transferCount) {
        return new SubwayTravelTimeResponse(start, destination, totalTime, transferCount);
    }

    public static SubwayTravelTimeResponse from(SubwayTravelTime entity) {
        return new SubwayTravelTimeResponse(
                entity.getSubA().getName(),
                entity.getSubB().getName(),
                entity.getTotalTime(),
                entity.getTransferCount()
        );
    }
}
