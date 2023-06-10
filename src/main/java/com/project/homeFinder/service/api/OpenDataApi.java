package com.project.homeFinder.service.api;

import com.project.homeFinder.dto.response.raw.xml.StanReginCdXmlResponseRaw;
import com.project.homeFinder.dto.response.raw.xml.ApartmentBasicInfoXmlResponseRaw;
import com.project.homeFinder.dto.response.raw.xml.ApartmentListResponseRaw;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenDataApi {

    @Value("${openData.api.key}")
    private String OPEN_DATA_API_KEY;

    private final WebClient webClient;

    public ApartmentListResponseRaw openDataFindAllApt(int page, int size) {
        final String uri =
                String.format("http://apis.data.go.kr/1613000/AptListService2/getTotalAptList?serviceKey=%s&pageNo=%d&numOfRows=%d", OPEN_DATA_API_KEY, page, size);

        try {
            return webClient.get()
                    .uri(new URI(uri)) // 공공데이터는 URI 를 String 그대로 요청을 보내면 'SERVICE_KEY_IS_NOT_REGISTERED_ERROR' 오류 발생 -> URI 객체로 생성해서 요청 보내기
                    .accept(MediaType.APPLICATION_XML)
                    .retrieve()
                    .bodyToMono(ApartmentListResponseRaw.class)
                    .single().blockOptional()
                    .orElse(new ApartmentListResponseRaw(null, null));

        } catch (Exception e) {
            throw new RuntimeException("[OpenDataApi]->{openDataFindAllApt} | Error. Could not fetch Open Data.");
        }
    }

    public ApartmentListResponseRaw openDataFindAllApt() {
        return openDataFindAllApt(1, 10);
    }

    // 국토교통부_법정동 아파트 목록
    public ApartmentListResponseRaw openDataFindAllAptBjdCode(int page, int size, String bjdCode) {
        final String uri =
                String.format("https://apis.data.go.kr/1613000/AptListService2/getLegaldongAptList?serviceKey=%s&pageNo=%d&numOfRows=%d&bjdCode=%s",
                        OPEN_DATA_API_KEY, page, size, bjdCode);

        try {
            return webClient.get()
                    .uri(new URI(uri)) // 공공데이터는 URI 를 String 그대로 요청을 보내면 'SERVICE_KEY_IS_NOT_REGISTERED_ERROR' 오류 발생 -> URI 객체로 생성해서 요청 보내기
                    .accept(MediaType.APPLICATION_XML)
                    .retrieve()
                    .bodyToMono(ApartmentListResponseRaw.class)
                    .single().blockOptional()
                    .orElse(new ApartmentListResponseRaw(null, null));

        } catch (Exception e) {
            throw new RuntimeException("[OpenDataApi]->{openDataFindAllApt} | Error. Could not fetch Open Data.");
        }
    }

    public ApartmentBasicInfoXmlResponseRaw openDataAptBasicInfo(String kaptCode){
        final String uri =
                String.format("https://apis.data.go.kr/1613000/AptBasisInfoService1/getAphusBassInfo?serviceKey=%s&kaptCode=%s",
                        OPEN_DATA_API_KEY, kaptCode);

        try {
            return webClient.get()
                    .uri(new URI(uri)) // 공공데이터는 URI 를 String 그대로 요청을 보내면 'SERVICE_KEY_IS_NOT_REGISTERED_ERROR' 오류 발생 -> URI 객체로 생성해서 요청 보내기
                    .accept(MediaType.APPLICATION_XML)
                    .retrieve()
                    .bodyToMono(ApartmentBasicInfoXmlResponseRaw.class)
                    .single().blockOptional()
                    .orElse(new ApartmentBasicInfoXmlResponseRaw());

        } catch (Exception e) {
            throw new RuntimeException("[OpenDataApi]->{openDataAptBasicInfo} | Error. Could not fetch Open Data.");
        }
    }

    /*
    https://www.data.go.kr/bbs/dnb/selectDeveloperNetworkView.do?pageIndex=1&searchOrder=&searchCondition1=&reqestSn=QUES_000000000002570&tabGubun=DN0100&sort-post=on&searchKeyword1=
    04 HTTP ROUTING ERROR 해결.. => APPLICATION_XML -> TEXT_HTML 변경
     */
    public StanReginCdXmlResponseRaw openDataStanRegin(int page, int size) {
        final String uri =
                String.format("https://apis.data.go.kr/1741000/StanReginCd/getStanReginCdList?serviceKey=%s&pageNo=%s&numOfRows=%s&type=xml",
                        OPEN_DATA_API_KEY, page, size);

        try {
            return webClient.get()
                    .uri(new URI(uri))
                    .accept(MediaType.TEXT_HTML)
                    .retrieve()
                    .bodyToMono(StanReginCdXmlResponseRaw.class)
                    .single().blockOptional()
                    .orElse(new StanReginCdXmlResponseRaw());

        } catch (Exception e) {
            throw new RuntimeException("[OpenDataApi]->{openDataStanRegin} | Error. Could not fetch Open Data.");
        }
    }
}
