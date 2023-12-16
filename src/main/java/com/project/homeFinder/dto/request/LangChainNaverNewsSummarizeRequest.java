package com.project.homeFinder.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LangChainNaverNewsSummarizeRequest {

    public String question;
    public Map<String, String> datas;

    public static LangChainNaverNewsSummarizeRequest of(String question, Map<String, String> datas) {
        return new LangChainNaverNewsSummarizeRequest(question, datas);
    }

}
