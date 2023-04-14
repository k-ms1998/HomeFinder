package com.project.homeFinder.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SingleDestinationResponse {

    private TotalTimeAndTransferCount totalTimeAndTransferCount;

    public static SingleDestinationResponse of(TotalTimeAndTransferCount totalTimeAndTransferCount) {
        return new SingleDestinationResponse(totalTimeAndTransferCount);
    }
}
