package com.project.homeFinder.dto.request;

import com.project.homeFinder.dto.Point;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MultipleDestinationsRequest {

    private Point start;
    private List<Point> destinations;

}
