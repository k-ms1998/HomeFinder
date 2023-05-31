package com.project.homeFinder.service.api;

import com.project.homeFinder.dto.response.raw.xml.ApartmentBasicInfoXmlResponseRaw;
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

    public ApartmentBasicInfoXmlResponseRaw openDataFindAllApt(int page, int size) {
        final String uri =
                String.format("http://apis.data.go.kr/1613000/AptListService2/getTotalAptList?serviceKey=%s&pageNo=%d&numOfRows=%d", OPEN_DATA_API_KEY, page, size);

        try {
            return webClient.get()
                    .uri(new URI(uri)) // 공공데이터는 URI 를 String 그대로 요청을 보내면 'SERVICE_KEY_IS_NOT_REGISTERED_ERROR' 오류 발생 -> URI 객체로 생성해서 요청 보내기
                    .accept(MediaType.APPLICATION_XML)
                    .retrieve()
                    .bodyToMono(ApartmentBasicInfoXmlResponseRaw.class)
                    .single().blockOptional()
                    .orElse(new ApartmentBasicInfoXmlResponseRaw(null, null));

        } catch (Exception e) {
            throw new RuntimeException("[OpenDataApi]->{openDataFindAllApt} | Error. Could not fetch Open Data.");
        }
    }

    public ApartmentBasicInfoXmlResponseRaw openDataFindAllApt() {
        return openDataFindAllApt(1, 10);
    }

    // 국토교통부_법정동 아파트 목록
    public ApartmentBasicInfoXmlResponseRaw openDataFindAllAptBjdCode(int page, int size, String bjdCode) {
        final String uri =
                String.format("https://apis.data.go.kr/1613000/AptListService2/getLegaldongAptList?serviceKey=%s&pageNo=%d&numOfRows=%d&bjdCode=%s",
                        OPEN_DATA_API_KEY, page, size, bjdCode);

        try {
            return webClient.get()
                    .uri(new URI(uri)) // 공공데이터는 URI 를 String 그대로 요청을 보내면 'SERVICE_KEY_IS_NOT_REGISTERED_ERROR' 오류 발생 -> URI 객체로 생성해서 요청 보내기
                    .accept(MediaType.APPLICATION_XML)
                    .retrieve()
                    .bodyToMono(ApartmentBasicInfoXmlResponseRaw.class)
                    .single().blockOptional()
                    .orElse(new ApartmentBasicInfoXmlResponseRaw(null, null));

        } catch (Exception e) {
            throw new RuntimeException("[OpenDataApi]->{openDataFindAllApt} | Error. Could not fetch Open Data.");
        }
    }
}
