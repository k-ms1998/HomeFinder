package com.project.homeFinder.batch.job.step.reader;

import com.project.homeFinder.dto.response.raw.xml.ApartmentBasicInfoXmlResponseRaw;
import com.project.homeFinder.service.api.OpenDataApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

@Slf4j
@RequiredArgsConstructor
public class OpenDataApiApartmentBasicInfoReader implements ItemReader<ApartmentBasicInfoXmlResponseRaw> {

    private final OpenDataApi openDataApi;

    /**
     * Reader는 read()에서 반환된 값이 null일때까지 계속 실행됨
     * -> 종료 조건을 만들어줘야함
     */
    @Override
    public ApartmentBasicInfoXmlResponseRaw read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return openDataApi.openDataFindAllApt();
    }
}
