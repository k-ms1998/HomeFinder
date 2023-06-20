package com.project.homeFinder.dto.cache;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SerializeListOfApartmentToSubway {

    public List<ApartmentToSubwayCacheDto> data;

    public static SerializeListOfApartmentToSubway of(List<ApartmentToSubwayCacheDto> data) {
        return new SerializeListOfApartmentToSubway(data);
    }
}
