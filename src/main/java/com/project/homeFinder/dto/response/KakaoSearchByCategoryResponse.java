package com.project.homeFinder.dto.response;

import com.project.homeFinder.dto.response.raw.KakaoSearchByCategoryResponseRaw;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class KakaoSearchByCategoryResponse {

    private String name;
    private String x;
    private String y;
    private String distance;

    public static List<KakaoSearchByCategoryResponse> fromDocumentList(List<KakaoSearchByCategoryResponseRaw.Place> raw) {
        return raw.stream()
                .map(KakaoSearchByCategoryResponse::fromDocument)
                .collect(Collectors.toList());
    }


    public static KakaoSearchByCategoryResponse fromDocument(KakaoSearchByCategoryResponseRaw.Place raw) {
        return new KakaoSearchByCategoryResponse(raw.getPlace_name(), raw.getX(), raw.getY(), raw.getDistance());
    }
}
