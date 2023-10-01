package com.project.homeFinder.service;

import com.project.homeFinder.dto.response.raw.xml.ApartmentBasicInfoXmlItem;
import com.project.homeFinder.dto.response.raw.xml.ApartmentListXmlItem;
import com.project.homeFinder.dto.response.raw.xml.ApartmentListResponseRaw;
import com.project.homeFinder.repository.ApartmentRepository;
import com.project.homeFinder.service.api.OpenDataApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApartmentService {

    private final OpenDataApi openDataApi;
    private final ApartmentRepository apartmentRepository;

    public List<ApartmentListXmlItem> findAllAptInfoOpenDataApi() throws URISyntaxException {
        ApartmentListResponseRaw apartmentListResponseRaw = openDataApi.openDataFindAllApt();
        List<ApartmentListXmlItem> apartmentListXmlItems = apartmentListResponseRaw.fetchItems();

        return apartmentListXmlItems;
    }

    public List<ApartmentListXmlItem> findAllAptInfoOpenDataApiBjdCode(String bjdCode) throws URISyntaxException {
        ApartmentListResponseRaw apartmentListResponseRaw = openDataApi.openDataFindAllAptBjdCode(1, 10, bjdCode);
        List<ApartmentListXmlItem> apartmentListXmlItems = apartmentListResponseRaw.fetchItems();

        return apartmentListXmlItems;
    }

    public ApartmentBasicInfoXmlItem findAptBasicInfoApi(String kaptCode) {
        if(kaptCode.equals("") || kaptCode.isBlank() || kaptCode == null){
            throw new RuntimeException("KapCode required.");
        }

        return openDataApi.openDataAptBasicInfo(kaptCode).toItem();
    }

    public void compareApartments(List<String> id) {
        if(id.size() <= 1){
            return;
        }

        List<Long> ids = new ArrayList<>();
        for(String s : id){
            try {
                ids.add(Long.parseLong(s));
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Please input the correct apartment id.");
            }
        }

        apartmentRepository.fetchMultipleApartments(ids);
    }

}
