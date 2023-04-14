package com.project.homeFinder.repository;

import com.project.homeFinder.domain.SubwayTravelTime;
import com.project.homeFinder.repository.custom.SubwayTravelTimeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubwayTravelTimeRepository extends JpaRepository<SubwayTravelTime, Long>, SubwayTravelTimeRepositoryCustom {
}
