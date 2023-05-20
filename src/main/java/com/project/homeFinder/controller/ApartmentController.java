package com.project.homeFinder.controller;

import com.project.homeFinder.dto.response.raw.xml.ApartmentBasicInfoXmlItem;
import com.project.homeFinder.service.ApartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/apartment")
public class ApartmentController {

    private final ApartmentService apartmentService;

    @GetMapping("/open_data/find/all")
    public ResponseEntity<List<ApartmentBasicInfoXmlItem>> findAllAptInfoOpenDataApi() throws URISyntaxException {
        return ResponseEntity.ok(apartmentService.findAllAptInfoOpenDataApi());
    }

}
