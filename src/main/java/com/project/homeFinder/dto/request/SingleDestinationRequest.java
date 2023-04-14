package com.project.homeFinder.dto.request;

import com.project.homeFinder.dto.Point;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SingleDestinationRequest {

    private Point start;
    private Point destination;

    public static SingleDestinationRequest of(Point start, Point destination) {
        return new SingleDestinationRequest(start, destination);
    }
}
