package com.project.homeFinder.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Subway {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String line;
    private String name;
    private String x;
    private String y;


    public Subway(String line, String name, String x, String y) {
        this.line = line;
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public static Subway of(String line, String name, String x, String y) {
        return new Subway(line, name, x, y);
    }

    public void updateX(String x) {
        this.x = x;
    }

    public void updateY(String y) {
        this.y = y;
    }
}
