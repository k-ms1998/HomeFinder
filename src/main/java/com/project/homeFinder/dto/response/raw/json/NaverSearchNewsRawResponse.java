package com.project.homeFinder.dto.response.raw.json;

import com.project.homeFinder.dto.response.raw.NaverSearchNewsItem;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NaverSearchNewsRawResponse {

    private String lastBuildDate;
    private int total;
    private int start;
    private int display;
    private List<Item> items;

    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Item {

        private String title;
        private String originalink;
        private String link;
        private String description;
        private String pubDate;

    }

    public List<NaverSearchNewsItem> fetchItems(){
        return this.items.stream()
                .map(i -> NaverSearchNewsItem.of(
                        i.title,
                        i.originalink,
                        i.link,
                        i.description,
                        i.pubDate
                )).collect(Collectors.toList());
    }

}
