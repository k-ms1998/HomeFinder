package com.project.homeFinder.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {
        @Index(columnList = "regionCode")
})
public class BjdCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String regionCode;
    private String sidoCode;
    private String sggCode;
    private String umdCode;
    private String riCode;
    private String name;
    private String highCode;
    private String lowCode;

    public BjdCode(String regionCode, String sidoCode, String sggCode, String umdCode, String riCode, String name, String highCode, String lowCode) {
        this.regionCode = regionCode;
        this.sidoCode = sidoCode;
        this.sggCode = sggCode;
        this.umdCode = umdCode;
        this.riCode = riCode;
        this.name = name;
        this.highCode = highCode;
        this.lowCode = lowCode;
    }

    public static BjdCode of(String regionCode, String sidoCode, String sggCode, String umdCode, String riCode, String name, String highCode, String lowCode) {
        return new BjdCode(
                regionCode,
                sidoCode,
                sggCode,
                umdCode,
                riCode,
                name,
                highCode,
                lowCode
        );
    }
}
