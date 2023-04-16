package com.project.homeFinder.repository.custom.impl;

import com.project.homeFinder.domain.Subway;
import com.project.homeFinder.domain.SubwayTravelTime;
import com.project.homeFinder.repository.custom.SubwayTravelTimeRepositoryCustom;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SubwayTravelTimeRepositoryCustomImpl implements SubwayTravelTimeRepositoryCustom {

    private final EntityManager em;

    @Override
    public Optional<SubwayTravelTime> findBySubwayToSubway(Subway subwayA, Subway subwayB) {
        // WHERE (subA = subwayA AND subB = subwayB_ OR (subA = subwayB AND subB = subwayA)
        return Optional.ofNullable(em.createQuery("SELECT s FROM SubwayTravelTime s WHERE s.subA=:subwayA AND s.subB=:subwayB", SubwayTravelTime.class)
                .setParameter("subwayA", subwayA)
                .setParameter("subwayB", subwayB)
                .getResultStream().findFirst()
                .orElse(null));

    }

    @Override
    public List<SubwayTravelTime> findByTimeFromSubway(Subway subway, Long time) {
        return em.createQuery("SELECT s FROM SubwayTravelTime s WHERE (s.subA=:subway OR s.subB=:subway) AND s.totalTime<=:time", SubwayTravelTime.class)
                .setParameter("subway", subway)
                .setParameter("time", time)
                .getResultList();
    }

}
