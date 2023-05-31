package com.project.homeFinder.batch.job.step.reader;

import com.project.homeFinder.dto.response.raw.xml.ApartmentListResponseRaw;
import com.project.homeFinder.service.api.OpenDataApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.*;

@Slf4j
@RequiredArgsConstructor
public class OpenDataApiApartmentBasicInfoReader implements ItemReader<ApartmentListResponseRaw> {

    private final OpenDataApi openDataApi;

    private static String BJD_CODE = "";
    private static int page = 1;
    private static int size = 10;
    private static boolean IS_END = false;

    /**
     * Reader는 read()에서 반환된 값이 null일때까지 계속 실행됨
     * -> 종료 조건을 만들어줘야함
     */
    @Override
    public ApartmentListResponseRaw read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (IS_END) {
            return null;
        }

        ApartmentListResponseRaw data = null;
        if(BJD_CODE.equals("") || BJD_CODE.isBlank()){
             data = openDataApi.openDataFindAllApt(page, size);
        }else{
            data = openDataApi.openDataFindAllAptBjdCode(page, size, BJD_CODE);
        }

        log.info("[OpenDataApiApartmentBasicInfoReader] currentPage={}", page);
        if (data.fetchItems().size() == 0 || page > 5) {
            return null;
        }
        if(data.fetchItems().size() < size){
            IS_END = true;
        }

        ++page;
        return data;
    }

    public void updateBjdCode(String bjdCode) {
        this.BJD_CODE = bjdCode;
    }
}
