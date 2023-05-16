package com.project.homeFinder.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subway_travel_time")
public class SubwayTravelTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Subway subA;

    @OneToOne(fetch = FetchType.LAZY)
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

    @Override
    public String toString() {
        return "SubwayTravelTime{" +
                "id=" + id +
                ", subA=" + subA.getName() +
                ", subB=" + subB.getName() +
                ", totalTime=" + totalTime +
                ", transferCount=" + transferCount +
                '}';
    }
}
