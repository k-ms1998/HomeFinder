package com.project.homeFinder.repository.custom.impl;

import com.project.homeFinder.domain.Apartment;
import com.project.homeFinder.repository.custom.ApartmentRepositoryCustom;

import java.util.List;

public class ApartmentRepositoryCustomImpl implements ApartmentRepositoryCustom {


    @Override
    public List<Apartment> fetchMultipleApartments(List<Long> ids) {
        return null;
    }
}
