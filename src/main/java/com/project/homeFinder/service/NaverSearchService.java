package com.project.homeFinder.service;

import com.project.homeFinder.domain.Apartment;
import com.project.homeFinder.domain.BjdCode;
import com.project.homeFinder.dto.response.raw.NaverSearchNewsItem;
import com.project.homeFinder.repository.ApartmentRepository;
import com.project.homeFinder.repository.BjdCodeRepository;
import com.project.homeFinder.service.api.NaverApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NaverSearchService {

    private final NaverApi naverApi;

    private final ApartmentRepository apartmentRepository;
    private final BjdCodeRepository bjdCodeRepository;

    public List<NaverSearchNewsItem> naverSearchNewsByArea(Long aptId, int display, int start, String sort){
        if(aptId == null){
            throw new RuntimeException("Please input the apartment id.");
        }

        Apartment apartment = apartmentRepository.getReferenceById(aptId);
        String dong = apartment.getDong(); // 1171010100 -> bjdCode의 regionCode

        BjdCode bjdCode = bjdCodeRepository.findFirstByRegionCode(dong);
        String lowCode = bjdCode.getLowCode();

        List<NaverSearchNewsItem> newsItems = naverApi.fetchNaverNews(lowCode, display, start, sort).fetchItems();

        return newsItems;

    }

}
