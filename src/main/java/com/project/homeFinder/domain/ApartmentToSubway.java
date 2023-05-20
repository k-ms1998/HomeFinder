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

    private Long time;


    public ApartmentToSubway of(Apartment apartment, Subway subway, Long time) {
        return new ApartmentToSubway(apartment, subway, time);
    }

    public ApartmentToSubway(Apartment apartment, Subway subway, Long time) {
        this.apartment = apartment;
        this.subway = subway;
        this.time = time;
    }

}
