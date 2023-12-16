package com.project.homeFinder.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LangChainParams {
    LANG_CHAIN_BASE_URL("http://localhost:8000/"),
    LANG_CHAIN_NAVER_NEWS_SUMMARIZE_URL_V1("homefinder/apartment/news/v1"),
    LANG_CHAIN_NAVER_NEWS_SUMMARIZE_QUESTION("요약해서 알려줘");

    private String value;

}
