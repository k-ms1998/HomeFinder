package com.project.homeFinder.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subway_travel_time")
public class SubwayTravelTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Subway subA;

    @OneToOne
    private Subway subB;

    private Long totalTime;
    private Long transferCount;

    public SubwayTravelTime(Subway subA, Subway subB, Long totalTime, Long transferCount) {
        this.subA = subA;
        this.subB = subB;
        this.totalTime = totalTime;
        this.transferCount = transferCount;
    }

    public static SubwayTravelTime of(Subway subA, Subway subB, Long totalTime, Long transferCount) {

        return new SubwayTravelTime(subA, subB, totalTime, transferCount);
    }
}
