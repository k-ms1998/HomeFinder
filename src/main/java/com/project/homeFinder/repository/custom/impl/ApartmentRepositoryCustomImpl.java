package com.project.homeFinder.repository.custom.impl;

import com.project.homeFinder.domain.Apartment;
import com.project.homeFinder.repository.custom.ApartmentRepositoryCustom;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ApartmentRepositoryCustomImpl implements ApartmentRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Apartment> fetchMultipleApartments(List<Long> ids) {

        String query = "SELECT a FROM Apartment a where a.id in :ids";

        return em.createQuery(query)
                .setParameter("ids", ids)
                .getResultList();
    }

}
