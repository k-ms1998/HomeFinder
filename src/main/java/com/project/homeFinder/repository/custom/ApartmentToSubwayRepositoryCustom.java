package com.project.homeFinder.repository.custom;

import com.project.homeFinder.domain.ApartmentToSubway;
import com.project.homeFinder.domain.Subway;
import com.project.homeFinder.domain.SubwayTravelTime;

import java.util.List;

public interface ApartmentToSubwayRepositoryCustom {

    List<ApartmentToSubway> findBySubway(Subway subway);

    List<ApartmentToSubway> findBySubwayAndTime(Subway subway, Long time);

}
