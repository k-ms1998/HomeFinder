package com.project.homeFinder.repository;

import com.project.homeFinder.domain.BusStation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusStationRepository extends JpaRepository<BusStation, Long> {
}
