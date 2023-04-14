package com.project.homeFinder.repository.custom;

import com.project.homeFinder.domain.Subway;
import com.project.homeFinder.domain.SubwayTravelTime;
import com.project.homeFinder.repository.SubwayTravelTimeRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubwayTravelTimeRepositoryCustom{

    Optional<SubwayTravelTime> findBySubwayToSubway(Subway subwayA, Subway subwayB);
}
