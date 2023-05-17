package com.project.homeFinder.repository.custom.impl;

import com.project.homeFinder.domain.ApartmentToSubway;
import com.project.homeFinder.domain.Subway;
import com.project.homeFinder.repository.custom.ApartmentToSubwayRepositoryCustom;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class ApartmentToSubwayRepositoryCustomImpl implements ApartmentToSubwayRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<ApartmentToSubway> findBySubway(Subway subway) {
        return em.createQuery("SELECT a FROM ApartmentToSubway a" +
                        " JOIN FETCH a.apartment" +
                        " WHERE a.subway = :subway" , ApartmentToSubway.class)
                .setParameter("subway", subway)
                .getResultList();
    }

    @Override
    public List<ApartmentToSubway> findBySubwayAndTime(Subway subway, Long time) {
        return em.createQuery("SELECT a FROM ApartmentToSubway a" +
                " JOIN FETCH a.apartment" +
                " WHERE a.subway = :subway AND a.time = :time" , ApartmentToSubway.class)
                .setParameter("subway", subway)
                .setParameter("time", time)
                .getResultList();
    }

}
