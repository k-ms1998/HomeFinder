package com.project.homeFinder.dto.response.raw;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NaverSearchNewsItem {

    private String title;
    private String originalink;
    private String link;
    private String description;
    private String pubDate;

    public static NaverSearchNewsItem of(String title, String originalink, String link, String description, String pubDate) {
        return new NaverSearchNewsItem(
                title,
                originalink,
                link,
                description,
                pubDate
        );
    }
}
