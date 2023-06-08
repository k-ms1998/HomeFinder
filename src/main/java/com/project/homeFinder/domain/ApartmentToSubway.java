package com.project.homeFinder.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApartmentToSubway {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Apartment apartment;

    @ManyToOne(fetch = FetchType.LAZY)
    private Subway subway;

    private Long distance;
    private Long time;

    public static ApartmentToSubway of(Apartment apartment, Subway subway, Long time) {
        return ApartmentToSubway.of(apartment, subway, -1L, time);
    }

    public static ApartmentToSubway of(Apartment apartment, Subway subway, Long distance, Long time) {
        return new ApartmentToSubway(apartment, subway, distance, time);
    }

    public ApartmentToSubway(Apartment apartment, Subway subway, Long distance, Long time) {
        this.apartment = apartment;
        this.subway = subway;
        this.distance = distance;
        this.time = time;
    }

    @Override
    public String toString() {
        return "ApartmentToSubway{" +
                "id=" + id +
                ", apartment=" + apartment.getName() +
                ", subway=" + subway.getName() +
                ", time=" + time +
                '}';
    }
}
