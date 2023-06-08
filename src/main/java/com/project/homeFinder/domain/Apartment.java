package com.project.homeFinder.domain;

import com.project.homeFinder.dto.response.raw.xml.ApartmentListXmlItem;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Apartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String aptCode;
    private String bjdCode;
    private String dong;
    private String name;
    private String address;
    private String date;
    private String x;
    private String y;

    public static Apartment of(String aptCode, String bjdCode, String dong, String name, String address, String date) {
        return Apartment.of(aptCode, bjdCode, dong, name, address, date, null, null);
    }

    public static Apartment of(String aptCode, String bjdCode, String dong, String name, String address, String date, String x, String y) {
        return new Apartment(null, aptCode, bjdCode, dong, name, address, date, x, y);
    }

    public void updateX(String x) {
        this.x = x;
    }

    public void updateY(String y) {
        this.y = y;
    }

    public void updateXY(String x, String y) {
        this.x = x;
        this.y = y;
    }

    public static Apartment fromXml(ApartmentListXmlItem xml) {
        return Apartment.of(
                xml.getKaptCode(),
                xml.getBjdCode(),
                xml.getAs3(),
                xml.getKaptName(),
                "address",
                "date"
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Apartment apartment = (Apartment) o;
        return Objects.equals(getId(), apartment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
