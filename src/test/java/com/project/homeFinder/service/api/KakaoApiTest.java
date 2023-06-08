package com.project.homeFinder.service.api;

import com.project.homeFinder.dto.response.KakaoSearchByAddressResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;

@Import(KakaoApi.class)
@SpringBootTest
class KakaoApiTest {

    private final KakaoApi kakaoApi;

    @Autowired
    public KakaoApiTest(KakaoApi kakaoApi) {
        this.kakaoApi = kakaoApi;
    }

    @Test
    void findByAddress() {
        final String address = "올림픽로 135";

        List<KakaoSearchByAddressResponse> byAddress = kakaoApi.findByAddress(address);
        KakaoSearchByAddressResponse firstResponse = byAddress.get(0);
        Assertions.assertThat(firstResponse)
                .hasFieldOrPropertyWithValue("roadAddress", "서울 송파구 올림픽로 135")
                .hasFieldOrPropertyWithValue("x", "127.089889095677")
                .hasFieldOrPropertyWithValue("y", "37.516733284228");
    }
}