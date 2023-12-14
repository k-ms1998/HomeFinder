package com.project.homeFinder.dto.response.raw;

import com.project.homeFinder.domain.RegionNews;
import com.project.homeFinder.dto.response.LangChainNaverNewsSummarizeResponse;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LangChainNaverNewsSummarizeResponseRaw {

    public String question;
    public String answer;
    public String sources;

    public static LangChainNaverNewsSummarizeResponseRaw of(String question, String answer, String sources) {
        return new LangChainNaverNewsSummarizeResponseRaw(question, answer, sources);
    }

    public LangChainNaverNewsSummarizeResponse toClientResponse() {
        return LangChainNaverNewsSummarizeResponse.of(this.answer);
    }

    public static LangChainNaverNewsSummarizeResponseRaw fromRegionNews(RegionNews regionNews) {
        return LangChainNaverNewsSummarizeResponseRaw.of("[Empty]", regionNews.getNews(), "[DB]");
    }

    public static LangChainNaverNewsSummarizeResponseRaw createErrorResponse(String errorMsg) {
        return LangChainNaverNewsSummarizeResponseRaw.of(
                "[Error]",
                String.format("[Error], %s", errorMsg),
                null
        );
    }
}
