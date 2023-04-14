package com.project.homeFinder.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TotalTimeAndTransferCount {

    private Long totalTime;
    private Long transferCount;

    public static TotalTimeAndTransferCount of(Long totalTime, Long transferCount) {
        return new TotalTimeAndTransferCount(totalTime, transferCount);
    }

}
