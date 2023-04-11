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
public class MultipleDestinationResponse {

    private int size;
    private List<MultipleDestinationResponseBody> body;

    public static MultipleDestinationResponse of(int size,List<MultipleDestinationResponseBody> body) {
        return new MultipleDestinationResponse(size, body);
    }
}
