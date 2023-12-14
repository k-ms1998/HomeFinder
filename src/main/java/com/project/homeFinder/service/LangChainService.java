package com.project.homeFinder.service;

import com.project.homeFinder.domain.Apartment;
import com.project.homeFinder.domain.RegionNews;
import com.project.homeFinder.dto.request.LangChainNaverNewsSummarizeRequest;
import com.project.homeFinder.dto.response.raw.LangChainNaverNewsSummarizeResponseRaw;
import com.project.homeFinder.enums.LangChainParams;
import com.project.homeFinder.repository.ApartmentRepository;
import com.project.homeFinder.repository.RegionNewsRepository;
import com.project.homeFinder.repository.mapper.RegionNewsMapper;
import com.project.homeFinder.util.NaverUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LangChainService {

    private final WebClient webClient;
    private final NaverSearchService naverSearchService;

    private final RegionNewsMapper regionNewsMapper;

    private final ApartmentRepository apartmentRepository;
    private final RegionNewsRepository regionNewsRepository;

    @Transactional
    public LangChainNaverNewsSummarizeResponseRaw langChain_SummarizeNaverNewsV1(Long aptId) {
        RegionNews fromDB = regionNewsMapper.findRegionNewsFromTodayFindOne(LocalDate.now(), aptId);
        if(checkIsNotNull(fromDB)){
            return LangChainNaverNewsSummarizeResponseRaw.fromRegionNews(fromDB);
        }

        return fetchNaverNewsAndSummarizeAndSaveV1(aptId);
    }

    private LangChainNaverNewsSummarizeResponseRaw fetchNaverNewsAndSummarizeAndSaveV1(Long aptId) {
        LangChainNaverNewsSummarizeRequest request = createRequest(aptId);

        LangChainNaverNewsSummarizeResponseRaw result = webClient.mutate().build()
                .post().uri(LangChainParams.LANG_CHAIN_BASE_URL.getValue() + LangChainParams.LANG_CHAIN_NAVER_NEWS_SUMMARIZE_URL_V1.getValue())
                .accept(MediaType.APPLICATION_JSON).bodyValue(request)
                .retrieve().bodyToMono(LangChainNaverNewsSummarizeResponseRaw.class)
                .block();
        convertAndSaveAsRegionNews(aptId, result);

        return result;
    }

    private LangChainNaverNewsSummarizeRequest createRequest(Long aptId) {
        return LangChainNaverNewsSummarizeRequest.of(
                LangChainParams.LANG_CHAIN_NAVER_NEWS_SUMMARIZE_QUESTION.getValue(),
                NaverUtils.fetchLinkAndBody(
                        naverSearchService.naverSearchNewsByArea(aptId, 100, 1, "date")
                )
        );
    }

    private void convertAndSaveAsRegionNews(Long aptId, LangChainNaverNewsSummarizeResponseRaw input) {
        Apartment apartment = apartmentRepository.getReferenceById(aptId);
        String regionCode = apartment.getDong();

        regionNewsRepository.saveAndFlush(
                RegionNews.of(regionCode, input.getAnswer(), LocalDate.now())
        );

    }

    private <T> boolean checkIsNotNull(T input) {
        return input != null;
    }
}
