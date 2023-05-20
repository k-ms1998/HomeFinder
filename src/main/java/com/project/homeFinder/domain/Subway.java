package com.project.homeFinder.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

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

    @Override
    public String toString() {
        return "Subway{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subway subway = (Subway) o;
        return Objects.equals(getName(), subway.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
