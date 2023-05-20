package com.project.homeFinder.dto.response.raw.xml;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "response")
public class ApartmentBasicInfoXmlResponseRaw {

    @XmlElement(name = "header")
    private XmlHeader header;

    @XmlElement(name = "body")
    private XmlBody body;

    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class XmlBody{

        @XmlElement(name = "items")
        public XmlItems items;

        @Setter
        @ToString
        @NoArgsConstructor
        @AllArgsConstructor
        public static class XmlItems{

            public List<XmlItem> item;

            @Setter
            @ToString
            @NoArgsConstructor
            @AllArgsConstructor
            public static class XmlItem{

                @XmlElement(name = "as1")
                private String as1;

                @XmlElement(name = "as2")
                private String as2;

                @XmlElement(name = "as3")
                private String as3;

                @XmlElement(name = "as4")
                private String as4;

                @XmlElement(name = "bjdCode")
                private String bjdCode;

                @XmlElement(name = "kaptCode")
                private String kaptCode;

                @XmlElement(name = "kaptName")
                private String kaptName;

            }
        }
    }

    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class XmlHeader{
        @XmlElement(name = "resultCode")
        private String resultCode;

        @XmlElement(name = "resultMsg")
        private String resultMsg;
    }

    public List<ApartmentBasicInfoXmlItem> fetchItems(){
        return this.body.items.item.stream()
                .map(i -> ApartmentBasicInfoXmlItem.of(
                        i.as1,
                        i.as2,
                        i.as3,
                        i.as4,
                        i.bjdCode,
                        i.kaptCode,
                        i.kaptName
                )).collect(Collectors.toList());
    }

}
