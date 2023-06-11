package com.project.homeFinder.batch.job.step.reader;

import com.project.homeFinder.dto.response.raw.xml.ApartmentListResponseRaw;
import com.project.homeFinder.dto.response.raw.xml.StanReginCdXmlResponseRaw;
import com.project.homeFinder.service.api.OpenDataApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

@Slf4j
@RequiredArgsConstructor
public class StanReginCdReader implements ItemReader<StanReginCdXmlResponseRaw> {

    private final OpenDataApi openDataApi;

    private static int page = 1;
    private final int PAGE_SIZE = 100;
    private static boolean IS_END = false;

    /**
     * Reader는 read()에서 반환된 값이 null일때까지 계속 실행됨
     * -> 종료 조건을 만들어줘야함
     */
    @Override
    public StanReginCdXmlResponseRaw read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (IS_END) {
            return null;
        }

        log.info("[StanReginCdReader] currentPage={}", page);
        StanReginCdXmlResponseRaw data = openDataApi.openDataStanRegin(page, PAGE_SIZE);
        if(data.fetchRowCount() < PAGE_SIZE){
            IS_END = true;
        }

        ++page;
        return data;
    }

}
