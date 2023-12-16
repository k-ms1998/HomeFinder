package com.project.homeFinder.util;

import com.project.homeFinder.dto.response.raw.NaverSearchNewsItem;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NaverUtils {

    public static Map<String, String> fetchLinkAndBody(List<NaverSearchNewsItem> input) {
        return input.stream()
                .collect(Collectors.toMap(
                        i -> i.getLink(),
                        i -> i.getDescription()
                ));
    }

}
