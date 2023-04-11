package com.project.homeFinder.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MultipleDestinationResponseBody {

    private int count;
    private String name;
    private List<TotalTimeAndTransferCount> totalTimeAndTransferCount;

    public static MultipleDestinationResponseBody of(int count, String name, List<TotalTimeAndTransferCount> totalTimeAndTransferCount) {
        return new MultipleDestinationResponseBody(count, name, totalTimeAndTransferCount);
    }
}
