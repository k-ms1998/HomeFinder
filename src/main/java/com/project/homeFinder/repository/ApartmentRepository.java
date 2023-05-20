package com.project.homeFinder.repository;

import com.project.homeFinder.domain.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApartmentRepository extends JpaRepository<Apartment, Long> {



}
