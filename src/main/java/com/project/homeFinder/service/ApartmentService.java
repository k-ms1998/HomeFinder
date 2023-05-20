package com.project.homeFinder.service;

import com.project.homeFinder.dto.response.raw.xml.ApartmentBasicInfoXmlItem;
import com.project.homeFinder.dto.response.raw.xml.ApartmentBasicInfoXmlResponseRaw;
import com.project.homeFinder.service.api.OpenDataApi;
import jakarta.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApartmentService {

    private final OpenDataApi openDataApi;

    public List<ApartmentBasicInfoXmlItem> findAllAptInfoOpenDataApi() throws URISyntaxException {
        ApartmentBasicInfoXmlResponseRaw apartmentBasicInfoXmlResponseRaw = openDataApi.openDataFindAllApt();
        List<ApartmentBasicInfoXmlItem> apartmentBasicInfoXmlItems = apartmentBasicInfoXmlResponseRaw.fetchItems();

        return apartmentBasicInfoXmlItems;
    }

}
