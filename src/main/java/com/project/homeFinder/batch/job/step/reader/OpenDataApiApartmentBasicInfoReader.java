package com.project.homeFinder.batch.job.step.reader;

import com.project.homeFinder.dto.response.raw.xml.ApartmentBasicInfoXmlResponseRaw;
import com.project.homeFinder.service.api.OpenDataApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.*;

@Slf4j
@RequiredArgsConstructor
public class OpenDataApiApartmentBasicInfoReader implements ItemReader<ApartmentBasicInfoXmlResponseRaw> {

    private final OpenDataApi openDataApi;

    private int page = 1;
    private static int size = 10;

    /**
     * Reader는 read()에서 반환된 값이 null일때까지 계속 실행됨
     * -> 종료 조건을 만들어줘야함
     */
    @Override
    public ApartmentBasicInfoXmlResponseRaw read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        ApartmentBasicInfoXmlResponseRaw data = openDataApi.openDataFindAllApt(page, size);

        log.info("[OpenDataApiApartmentBasicInfoReader] currentPage={}", page);
        if (data.fetchItems().size() < size || page > 5) {
            return null;
        }

        ++page;
        return data;
    }
}
