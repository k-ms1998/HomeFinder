package com.project.homeFinder.repository;

import com.project.homeFinder.domain.BusStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BusStationRepository extends JpaRepository<BusStation, Long> {

    Optional<BusStation> findFirstByStationId(String stationId);

    @Query("SELECT DISTINCT b.stationId FROM BusStation b ORDER BY b.stationId")
    Set<String> fetchAllStationIds();

}
