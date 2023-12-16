package com.project.homeFinder.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LangChainNaverNewsSummarizeResponse {

    public String answer;

    public static LangChainNaverNewsSummarizeResponse of(String answer) {
        return new LangChainNaverNewsSummarizeResponse(answer);
    }

}
