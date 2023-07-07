package com.project.homeFinder.repository;

import com.project.homeFinder.domain.BusStation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusStationRepository extends JpaRepository<BusStation, Long> {

    Optional<BusStation> findByStationName(String stationName);

}
