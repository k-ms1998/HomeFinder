package com.project.homeFinder.service.api;

import com.project.homeFinder.dto.response.raw.xml.ApartmentBasicInfoXmlResponseRaw;
import com.project.homeFinder.dto.response.raw.xml.StanReginCdXmlResponseRaw;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(OpenDataApi.class)
@SpringBootTest
class OpenDataApiTest {

    private final OpenDataApi openDataApi;

    @Autowired
    public OpenDataApiTest(OpenDataApi openDataApi) {
        this.openDataApi = openDataApi;
    }

    @Test
    void aptBasicInfo(){
        ApartmentBasicInfoXmlResponseRaw response = openDataApi.openDataAptBasicInfo("A10027875");

        Assertions.assertThat(response).isNotNull();
    }

    @Test
    void stanReginCdCall(){
        StanReginCdXmlResponseRaw response = openDataApi.openDataStanRegin(1, 10);

        Assertions.assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("Given Nothing - When EndOfCallingOpenDataStanRegin - ThenSuccess")
    void givenNothing_whenEndOfCallingOpenDataStanRegin_thenSuccess() throws Exception {
        // Given
        int page = 1;
        final int SIZE = 100;

        // When
        while(true){
            StanReginCdXmlResponseRaw response = openDataApi.openDataStanRegin(page, SIZE);
            int count = response.fetchRowCount();
            if(count < SIZE){
                return;
            }
            page++;
        }

        // Then


    }
}