package com.project.homeFinder.repository.mapper;

import com.project.homeFinder.domain.RegionNews;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface RegionNewsMapper {

    RegionNews findRegionNewsFromTodayFindOne(@Param("today") LocalDate today, @Param("aptId") Long aptId);

}
