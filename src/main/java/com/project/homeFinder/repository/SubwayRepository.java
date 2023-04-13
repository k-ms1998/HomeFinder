package com.project.homeFinder.repository;

import com.project.homeFinder.domain.Subway;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubwayRepository extends JpaRepository<Subway, Long> {
    Optional<Subway> findByLineAndName(String line, String name);
}
