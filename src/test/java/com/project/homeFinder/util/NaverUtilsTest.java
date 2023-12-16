package com.project.homeFinder.util;

import com.project.homeFinder.dto.response.raw.NaverSearchNewsItem;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class NaverUtilsTest {

    @Test
    void givenListOfNaverSearchNewsItems_whenNothing_thenReturnMap() throws Exception {
        // Given
        final String TEST_BASE_TITLE = "title:";
        final String TEST_BASE_URL = "http://test.naverUtils.kms/";
        final String TEST_BASE_DESC = "Today is going to be great - Attempt:";

        List<NaverSearchNewsItem> items = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            items.add(NaverSearchNewsItem.of(
                    String.format("%s %d", TEST_BASE_TITLE, i),
                    String.format("%s%d", TEST_BASE_URL, i),
                    String.format("%s%d", TEST_BASE_URL, i),
                    String.format("%s %d", TEST_BASE_DESC, i),
                    LocalDate.now().toString()
            ));
        }

        // When
        Map<String, String> result = NaverUtils.fetchLinkAndBody(items);

        // Then
        for(String k : result.keySet()){
            System.out.println(String.format("%s -> %s", k, result.get(k)));
        }
    }

}