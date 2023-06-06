package com.project.homeFinder.dto.response.raw.xml;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "response")
public class ApartmentBasicInfoXmlResponseRaw {

    @XmlElement(name = "header")
    private XmlHeader header;

    @XmlElement(name = "body")
    private XmlBody body;

    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class XmlHeader {

        @XmlElement(name = "resultCode")
        private String resultCode;

        @XmlElement(name = "resultMsg")
        private String resultMsg;

    }

    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class XmlBody {

        @XmlElement(name = "item")
        private Item item;

        @ToString
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Item{
            @XmlElement(name = "bjdCode")
            private String bjdCode;

            @XmlElement(name = "codeAptNm")
            private String codeAptNm;

            @XmlElement(name = "codeHallNm")
            private String codeHallNm;

            @XmlElement(name = "codeHeatNm")
            private String codeHeatNm;

            @XmlElement(name = "codeMgrNm")
            private String codeMgrNm;

            @XmlElement(name = "codeSalesNm")
            private String codeSalesNm;

            @XmlElement(name = "doroJuso")
            private String doroJuso;

            @XmlElement(name = "hoCnt")
            private int hoCnt;

            @XmlElement(name = "kaptAcompany")
            private String kaptAcompany;

            @XmlElement(name = "kaptAddr")
            private String kaptAddr;

            @XmlElement(name = "kaptBcompany")
            private String kaptBcompany;

            @XmlElement(name = "kaptCode")
            private String kaptCode;

            @XmlElement(name = "kaptDongCnt")
            private int kaptDongCnt;

            @XmlElement(name = "kaptFax")
            private String kaptFax;

            @XmlElement(name = "kaptMarea")
            private double kaptMarea;

            @XmlElement(name = "kaptMparea_135")
            private double kaptMparea_135;

            @XmlElement(name = "kaptMparea_136")
            private double kaptMparea_136;

            @XmlElement(name = "kaptMparea_60")
            private double kaptMparea_60;

            @XmlElement(name = "kaptMparea_85")
            private double kaptMparea_85;

            @XmlElement(name = "kaptName")
            private String kaptName;

            @XmlElement(name = "kaptTarea")
            private double kaptTarea;

            @XmlElement(name = "kaptTel")
            private String kaptTel;

            @XmlElement(name = "kaptUrl")
            private String kaptUrl;

            @XmlElement(name = "kaptUsedate")
            private String kaptUsedate;

            @XmlElement(name = "kaptdaCnt")
            private int kaptdaCnt;

            @XmlElement(name = "privArea")
            private double privArea;

        }

    }

    public String fetchResultCode() {
        return this.header.resultCode;
    }

    public String fetchResultMsg() {
        return this.header.resultMsg;
    }

    public ApartmentBasicInfoXmlItem toItem() {
        XmlBody.Item item = this.body.item;
        return ApartmentBasicInfoXmlItem.of(
                item.bjdCode,
                item.codeAptNm,
                item.codeHallNm,
                item.codeHeatNm,
                item.codeMgrNm,
                item.codeSalesNm,
                item.doroJuso,
                item.hoCnt,
                item.kaptAcompany,
                item.kaptAddr,
                item.kaptCode,
                item.kaptDongCnt,
                item.kaptFax,
                item.kaptMarea,
                item.kaptMparea_135,
                item.kaptMparea_136,
                item.kaptMparea_60,
                item.kaptMparea_85,
                item.kaptName,
                item.kaptTarea,
                item.kaptTel,
                item.kaptUrl,
                item.kaptUsedate,
                item.kaptdaCnt,
                item.privArea
        );
    }
}

