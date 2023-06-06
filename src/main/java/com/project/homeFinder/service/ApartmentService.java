package com.project.homeFinder.service;

import com.project.homeFinder.dto.response.raw.xml.ApartmentListXmlItem;
import com.project.homeFinder.dto.response.raw.xml.ApartmentListResponseRaw;
import com.project.homeFinder.service.api.OpenDataApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApartmentService {

    private final OpenDataApi openDataApi;

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

}
