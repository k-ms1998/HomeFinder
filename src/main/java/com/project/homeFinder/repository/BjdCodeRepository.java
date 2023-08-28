package com.project.homeFinder.repository;

import com.project.homeFinder.domain.BjdCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BjdCodeRepository extends JpaRepository<BjdCode, Long> {

    Long countByRegionCode(String regionCode);

    Page<BjdCode> findAllByHighCode(String highCode, PageRequest pageRequest);

    @Query("SELECT b.regionCode FROM BjdCode b WHERE b.sidoCode=:sidoCode")
    List<String> findAllBySidoCode(String sidoCode);

    BjdCode findFirstByRegionCode(String regionCode);
}
