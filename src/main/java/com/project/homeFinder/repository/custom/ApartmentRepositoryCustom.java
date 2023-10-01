package com.project.homeFinder.repository.custom;

import com.project.homeFinder.domain.Apartment;

import java.util.List;

public interface ApartmentRepositoryCustom {

    List<Apartment> fetchMultipleApartments(List<Long> ids);
}
