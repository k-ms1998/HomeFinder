package com.project.homeFinder.repository;

import com.project.homeFinder.domain.Apartment;
import com.project.homeFinder.repository.custom.ApartmentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApartmentRepository extends JpaRepository<Apartment, Long>, ApartmentRepositoryCustom {

    Long countByName(String name);

}
