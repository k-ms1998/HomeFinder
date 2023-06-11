package com.project.homeFinder.dto.response.raw.xml;

import com.project.homeFinder.domain.BjdCode;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "StanReginCd")
public class StanReginCdXmlResponseRaw {

    @XmlElement(name = "head")
    private Head head;

    @XmlElement(name = "row")
    private List<Row> row;

    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Head {

        @XmlElement(name = "totalCount")
        private String totalCount;

        @XmlElement(name = "numOfRows")
        private String numOfRows;

        @XmlElement(name = "pageNo")
        private String pageNo;

        @XmlElement(name = "type")
        private String type;

        @XmlElement(name = "RESULT")
        private Result result;

        @ToString
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Result {

            @XmlElement(name = "resultCode")
            private String resultCode;

            @XmlElement(name = "resultMsg")
            private String resultMsg;
        }
    }

    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Row {

        @XmlElement(name = "region_cd")
        private String region_cd;

        @XmlElement(name = "sido_cd")
        private String sido_cd;

        @XmlElement(name = "sgg_cd")
        private String sgg_cd;

        @XmlElement(name = "umd_cd")
        private String umd_cd;

        @XmlElement(name = "ri_cd")
        private String ri_cd;

        @XmlElement(name = "locatjumin_cd")
        private String locatjumin_cd;

        @XmlElement(name = "locatjijuk_cd")
        private String locatjijuk_cd;

        @XmlElement(name = "locatadd_nm")
        private String locatadd_nm;

        @XmlElement(name = "locat_order")
        private String locat_order;

        @XmlElement(name = "locat_rm")
        private String locat_rm;

        @XmlElement(name = "locathigh_cd")
        private String locathigh_cd;

        @XmlElement(name = "locallow_nm")
        private String locallow_nm;

        @XmlElement(name = "adpt_de")
        private String adpt_de;
    }

    public int fetchRowCount() {
        return this.row.size();
    }

    public List<BjdCode> toEntity() {
        return this.row.stream()
                .map(r -> BjdCode.of(
                        r.region_cd,
                        r.sido_cd,
                        r.sgg_cd,
                        r.umd_cd,
                        r.ri_cd,
                        r.locatadd_nm,
                        r.locathigh_cd,
                        r.locallow_nm
                ))
                .collect(Collectors.toList());
    }
}