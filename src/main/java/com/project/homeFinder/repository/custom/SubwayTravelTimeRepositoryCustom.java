package com.project.homeFinder.repository.custom;

import com.project.homeFinder.domain.Subway;
import com.project.homeFinder.domain.SubwayTravelTime;

import java.util.List;
import java.util.Optional;

public interface SubwayTravelTimeRepositoryCustom{

    Optional<SubwayTravelTime> findBySubwayToSubway(Subway subwayA, Subway subwayB);

    List<SubwayTravelTime> findByTimeFromSubway(Subway subway, Long time);
}
