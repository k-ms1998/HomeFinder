package com.project.homeFinder.service.api;

import com.project.homeFinder.dto.response.raw.NaverSearchNewsItem;
import com.project.homeFinder.dto.response.raw.json.NaverSearchNewsRawResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class NaverApiTest {

    @Autowired
    public NaverApi naverApi;

    @Test
    void givenNothing_whenCallingFetchNaverNews_thenSuccess() throws Exception {
        // Given

        // When
        NaverSearchNewsRawResponse result = naverApi.fetchNaverNews("google", 10, 1, "sim");
        List<NaverSearchNewsItem> items = result.fetchItems();

        // Then
        System.out.println("result = " + result);
        System.out.println("items = " + items);
        Assertions.assertThat(items).isNotNull();

    }

    @Test
    void givenWrongParameters_whenCallingFetchNaverNews_thenFails() throws Exception {
        // Given
        final String query = "google";
        final Integer display = 10;
        final Integer start = 1;
        final String sort = "sim";

        final String invalidSort = "invalid";

        // When

        // Then
        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            naverApi.fetchNaverNews(null, display, start, sort);
        });

        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            naverApi.fetchNaverNews(query, null, start, sort);
        });

        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            naverApi.fetchNaverNews(query, display, null, sort);
        });

        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            naverApi.fetchNaverNews(query, display, start, null);
        });

        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            naverApi.fetchNaverNews(query, display, start, invalidSort);
        });


    }


}