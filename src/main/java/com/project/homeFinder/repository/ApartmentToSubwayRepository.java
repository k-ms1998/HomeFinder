package com.project.homeFinder.repository;

import com.project.homeFinder.domain.Apartment;
import com.project.homeFinder.domain.ApartmentToSubway;
import com.project.homeFinder.domain.Subway;
import com.project.homeFinder.repository.custom.ApartmentToSubwayRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApartmentToSubwayRepository extends JpaRepository<ApartmentToSubway, Long>, ApartmentToSubwayRepositoryCustom {

}
