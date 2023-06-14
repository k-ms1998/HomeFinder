package com.project.homeFinder.batch.job.step.reader;

import com.project.homeFinder.dto.response.raw.xml.ApartmentListResponseRaw;
import com.project.homeFinder.service.api.OpenDataApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

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
        if (IS_END) { // Read가 끝나는 시점
            IS_END = false; // Read가 끝나는 시점에서, 다시 IS_END를 초기화 시켜줘야, 다음 법정동 코드로 Read 할때 바로 종료가 안됨 
                            // -> 현재 Read가 끝나면, apartmentBasicInfoXmlResponseRawItemReader에서 OpenDataApiApartmentBasicInfoReader 객체를 새로 만들지 않고, 처음 호출 됐을때 생성된 객체를 다시 사용
                            //  ->파라미터 값들이 계속 유지됨 -> 그러므로, IS_END랑 page 값들을 초기화 시켜줘야 함
            page = 1;
            return null;
        }

        ApartmentListResponseRaw data = null;
        if(BJD_CODE.equals("") || BJD_CODE.isBlank()){
             data = openDataApi.openDataFindAllApt(page, size);
        }else{
            data = openDataApi.openDataFindAllAptBjdCode(page, size, BJD_CODE);
        }

        log.info("[OpenDataApiApartmentBasicInfoReader] BJD_CODE={}, currentPage={}", BJD_CODE, page);
        if (data.fetchItems().size() == 0) {
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
