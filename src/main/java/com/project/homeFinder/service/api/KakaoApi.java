package com.project.homeFinder.service.api;

import com.project.homeFinder.dto.Point;
import com.project.homeFinder.dto.response.KakaoSearchByAddressResponse;
import com.project.homeFinder.dto.response.KakaoSearchByCategoryResponse;
import com.project.homeFinder.dto.response.raw.KakaoSearchByCategoryResponseRaw;
import com.project.homeFinder.dto.response.raw.json.KakaoSearchByAddressResponseRaw;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class KakaoApi {

    @Value("${kakao.api.key}")
    private String KAKAO_API_KEY;

    private final WebClient webClient;

    public List<KakaoSearchByCategoryResponse> findToNearestSubway(Point request) {
        final String uri =
                String.format("https://dapi.kakao.com/v2/local/search/category.json?category_group_code=SW8&page=1&size=5&sort=distance&x=%s&y=%s&radius=10000", request.getX(), request.getY());

        return webClient.get()
                .uri(uri)
                .header("Authorization", "KakaoAK " + KAKAO_API_KEY)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(KakaoSearchByCategoryResponseRaw.class)
                .map(res -> res.getDocuments())
                .map(KakaoSearchByCategoryResponse::fromDocumentList)
                .single().blockOptional()
                .orElseThrow(() -> new RuntimeException("No Results."));

    }

    public List<KakaoSearchByAddressResponse> findByAddress(String address) {
        final String uri = String.format("https://dapi.kakao.com/v2/local/search/address.json?" +
                        "analyze_type=similar&page=1&size=10&query=%s", address);

        return webClient.get()
                .uri(uri)
                .header("Authorization", "KakaoAK " + KAKAO_API_KEY)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(KakaoSearchByAddressResponseRaw.class)
                .map(res -> res.getDocuments())
                .map(KakaoSearchByAddressResponse::fromDocumentList)
                .single().blockOptional()
                .orElseThrow(() -> new RuntimeException("No Results."));

    }
}
