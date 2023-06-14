package com.project.homeFinder.dto.response;

import com.project.homeFinder.dto.response.raw.json.KakaoSearchByAddressResponseRaw;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class KakaoSearchByAddressResponse {

    private String roadAddress;
    private String x;
    private String y;

    public static List<KakaoSearchByAddressResponse>  fromDocumentList(List<KakaoSearchByAddressResponseRaw.ComplexAddress> complexAddresses) {
        return complexAddresses.stream()
                .map(KakaoSearchByAddressResponse::fromComplexAddress)
                .collect(Collectors.toList());
    }

    public static KakaoSearchByAddressResponse fromComplexAddress(KakaoSearchByAddressResponseRaw.ComplexAddress complexAddress) {
        return new KakaoSearchByAddressResponse(
                addressExists(complexAddress) ? complexAddress.getRoad_address().getAddress_name() : "",
                complexAddress.getX(),
                complexAddress.getY()
        );
    }

    private static boolean addressExists(KakaoSearchByAddressResponseRaw.ComplexAddress complexAddress) {
        if(complexAddress == null){
            return false;
        }
        if(complexAddress.getRoad_address() == null){
            return false;
        }
        if(complexAddress.getRoad_address().getAddress_name() == null){
            return false;
        }

        return true;
    }
}
