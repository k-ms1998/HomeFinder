package com.project.homeFinder.dto.response.raw.xml;

import com.project.homeFinder.domain.Apartment;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ApartmentBasicInfoXmlItem {

    private String bjdCode;
    private String codeAptNm;
    private String codeHallNm;
    private String codeHeatNm;
    private String codeMgrNm;
    private String codeSalesNm;
    private String doroJuso;
    private int hoCnt;
    private String kaptAcompany;
    private String kaptAddr;
    private String kaptCode;
    private int kaptDongCnt;
    private String kaptFax;
    private double kaptMarea;
    private double kaptMparea_135;
    private double kaptMparea_136;
    private double kaptMparea_60;
    private double kaptMparea_85;
    private String kaptName;
    private double kaptTarea;
    private String kaptTel;
    private String kaptUrl;
    private String kaptUsedate;
    private int kaptdaCnt;
    private double privArea;

    public static ApartmentBasicInfoXmlItem of(String bjdCode, String codeAptNm, String codeHallNm, String codeHeatNm, String codeMgrNm,
                                               String codeSalesNm, String doroJuso, int hoCnt, String kaptAcompany, String kaptAddr, String kaptCode,
                                               int kaptDongCnt, String kaptFax, double kaptMarea, double kaptMparea_135, double kaptMparea_136,
                                               double kaptMparea_60, double kaptMparea_85, String kaptName, double kaptTarea, String kaptTel,
                                               String kaptUrl, String kaptUsedate, int kaptdaCnt, double privArea) {
        return new ApartmentBasicInfoXmlItem(
                bjdCode,
                codeAptNm,
                codeHallNm,
                codeHeatNm,
                codeMgrNm,
                codeSalesNm,
                doroJuso,
                hoCnt,
                kaptAcompany,
                kaptAddr,
                kaptCode,
                kaptDongCnt,
                kaptFax,
                kaptMarea,
                kaptMparea_135,
                kaptMparea_136,
                kaptMparea_60,
                kaptMparea_85,
                kaptName,
                kaptTarea,
                kaptTel,
                kaptUrl,
                kaptUsedate,
                kaptdaCnt,
                privArea
        );
    }

    public Apartment toEntity() {
        return Apartment.of(
                this.getKaptCode(),
                this.getBjdCode(),
                this.getBjdCode(),
                this.getKaptName(),
                this.getDoroJuso() != null ? this.getDoroJuso() : this.getKaptAddr(), // 공공데이터 미제공으로 도로주소가 주어지지 않는 경우 발생 예방
                this.getKaptUsedate()
        );
    }
}

