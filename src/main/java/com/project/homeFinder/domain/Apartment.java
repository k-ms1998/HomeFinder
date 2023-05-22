package com.project.homeFinder.domain;

import com.project.homeFinder.dto.response.raw.xml.ApartmentBasicInfoXmlItem;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Locale;
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

    public static Apartment of(String aptCode, String bjdCode, String dong, String name, String address, String date) {
        return new Apartment(null, aptCode, bjdCode, dong, name, address, date);
    }

    public static Apartment fromXml(ApartmentBasicInfoXmlItem xml) {
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
