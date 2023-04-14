package com.project.homeFinder.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Point {

    public String x;
    public String y;

    public static Point of(String x, String y) {
        return new Point(x, y);
    }
}
