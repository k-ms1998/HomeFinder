package com.project.homeFinder.service.api;

import com.project.homeFinder.dto.response.raw.json.NaverSearchNewsRawResponse;
import com.project.homeFinder.util.ServiceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverApi {

    @Value("${naver.api.id}")
    private String NAVER_API_ID;

    @Value("${naver.api.secret}")
    private String NAVER_API_SECRET;

    private String BASE_URL = "https://openapi.naver.com/";

    private final WebClient webClient;

    public NaverSearchNewsRawResponse fetchNaverNews(String query, int display, int start, String sort){
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(BASE_URL);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        String encodedQuery = ServiceUtils.encodeString(query);
        NaverSearchNewsRawResponse result = webClient.mutate() // mutate() => Return a builder to create a new WebClient whose settings are replicated from the current WebClient.
                .uriBuilderFactory(factory)
                .build()
                .get().uri(uriBuilder ->
                        uriBuilder.path("/v1/search/news.json?query={query}&display={display}&start={start}&sort={sort}")
                                .build(encodedQuery, display, start, sort))
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Naver-Client-Id", NAVER_API_ID)
                .header("X-Naver-Client-Secret", NAVER_API_SECRET)
                .retrieve()
                .bodyToMono(NaverSearchNewsRawResponse.class)
                .block();

        return result;
    }


}
