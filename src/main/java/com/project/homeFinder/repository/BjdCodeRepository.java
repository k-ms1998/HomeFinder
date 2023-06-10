package com.project.homeFinder.repository;

import com.project.homeFinder.domain.BjdCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BjdCodeRepository extends JpaRepository<BjdCode, Long> {

    Long countByRegionCode(String regionCode);

}
