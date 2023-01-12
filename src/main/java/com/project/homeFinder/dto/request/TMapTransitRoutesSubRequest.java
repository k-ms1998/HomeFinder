package com.project.homeFinder.dto.request;

import com.project.homeFinder.dto.response.raw.TMapTransitRoutesSubRawResponse;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TMapTransitRoutesSubRequest {

    @NotNull
    private String startX;

    @NotNull
    private String startY;

    @NotNull
    private String endX;

    @NotNull
    private String endY;

    private String format;
    private int count;

    public static TMapTransitRoutesSubRequest of(String startX, String startY, String endX, String endY) {
        return TMapTransitRoutesSubRequest.of(startX, startY, endX, endY, 10);
    }

    public static TMapTransitRoutesSubRequest of(String startX, String startY, String endX, String endY, int count) {
        return new TMapTransitRoutesSubRequest(
                startX,
                startY,
                endX,
                endY,
                "json",
                count
        );
    }
}
