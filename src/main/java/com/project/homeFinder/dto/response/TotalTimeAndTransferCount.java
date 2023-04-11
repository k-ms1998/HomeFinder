package com.project.homeFinder.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TotalTimeAndTransferCount {

    private Long totalTime;
    private Long transferCount;

    public static TotalTimeAndTransferCount of(Long totalTime, Long transferCount) {
        return new TotalTimeAndTransferCount(totalTime, transferCount);
    }

}
