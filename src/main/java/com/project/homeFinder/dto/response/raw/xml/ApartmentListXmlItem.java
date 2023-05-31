package com.project.homeFinder.dto.response.raw.xml;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApartmentListXmlItem {

    private String as1;
    private String as2;
    private String as3;
    private String as4;
    private String bjdCode;
    private String kaptCode;
    private String kaptName;

    public static ApartmentListXmlItem of(String as1, String as2, String as3, String as4, String bjdCode, String kaptCode, String kaptName) {
        return new ApartmentListXmlItem(as1, as2, as3, as4, bjdCode, kaptCode, kaptName);
    }
}
