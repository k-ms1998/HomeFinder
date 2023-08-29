package com.project.homeFinder.service;

import com.project.homeFinder.dto.response.raw.NaverSearchNewsItem;
import com.project.homeFinder.repository.BjdCodeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NaverSearchServiceTest {

    @Autowired
    private NaverSearchService service;

    @Test
    @Transactional
    void givenParameters_whenCallingNaverSearchNewsByArea_thenSuccess() throws Exception {
        // Given
        final Long aptId = 418L;
        final int display = 10;
        final int start = 1;
        final String sort = "sim";

        // When
        List<NaverSearchNewsItem> result = service.naverSearchNewsByArea(aptId, display, start, sort);

        // Then
        System.out.println("result = " + result);

    }

}