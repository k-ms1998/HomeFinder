package com.project.homeFinder.domain;

import com.project.homeFinder.dto.response.raw.LangChainNaverNewsSummarizeResponseRaw;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegionNews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "region_code")
    public String regionCode;
    public String news;
    public LocalDate date;

    public static RegionNews of(String regionCode, String news, LocalDate date) {
        return new RegionNews(null, regionCode, news, date);
    }

}
