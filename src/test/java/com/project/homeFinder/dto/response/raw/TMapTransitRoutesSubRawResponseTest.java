package com.project.homeFinder.dto.response.raw;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

class TMapTransitRoutesSubRawResponseTest {

    private final ObjectMapper mapper = new ObjectMapper();
    
    @DisplayName("[Service] Given Serialized Response - Returns MapTransitRoutesSubRawResponse")
    @Test
    void givenSerializedResponse_whenMapping_thenTMapTransitRoutesSubRawResponse() throws Exception {
        // Given
        String serializedResponse = """
                    {
                      "metaData": {
                        "requestParameters": {
                          "endY": "37.500420",
                          "endX": "127.126937",
                          "startY": "37.613444",
                          "startX": "126.926493",
                          "reqDttm": "20230111173949"
                        },
                        "plan": {
                          "itineraries": [
                            {
                              "fare": {
                                "regular": {
                                  "totalFare": 1650,
                                  "currency": {
                                    "symbol": "₩",
                                    "currency": "원",
                                    "currencyCode": "KRW"
                                  }
                                }
                              },
                              "walkDistance": 777,
                              "totalTime": 5124,
                              "walkTime": 972,
                              "transferCount": 1,
                              "totalDistance": 32612
                            },
                            {
                              "fare": {
                                "regular": {
                                  "totalFare": 1850,
                                  "currency": {
                                    "symbol": "₩",
                                    "currency": "원",
                                    "currencyCode": "KRW"
                                  }
                                }
                              },
                              "walkDistance": 1257,
                              "totalTime": 6053,
                              "walkTime": 1346,
                              "transferCount": 3,
                              "totalDistance": 28061
                            }
                          ]
                        }
                      }
                    }            
                """;
        Map<String, Object> attributes = mapper.readValue(serializedResponse, new TypeReference<>() {
        });

        // When
        TMapTransitRoutesSubRawResponse rawResponse = TMapTransitRoutesSubRawResponse.from(attributes);

        // Then
        Assertions.assertThat(rawResponse).isNotNull();

        /*
        Check MetaData
         */
        TMapTransitRoutesSubRawResponse.MetaData metaData = rawResponse.getMetaData();
        Assertions.assertThat(metaData).isNotNull();

        /*
        Check MetaData.RequestParameter
         */
        TMapTransitRoutesSubRawResponse.MetaData.RequestParameters requestParameters = metaData.getRequestParameters();
        Assertions.assertThat(requestParameters)
                .hasFieldOrPropertyWithValue("endY", "37.500420")
                .hasFieldOrPropertyWithValue("endX", "127.126937")
                .hasFieldOrPropertyWithValue("startY", "37.613444")
                .hasFieldOrPropertyWithValue("startX", "126.926493")
                .hasFieldOrPropertyWithValue("reqDttm", "20230111173949");

        /*
        Check MetaData.Plan
         */
        TMapTransitRoutesSubRawResponse.MetaData.Plan plan = metaData.getPlan();
        Assertions.assertThat(metaData).isNotNull();

        /*
        Check MetaData.Plan
         */
        TMapTransitRoutesSubRawResponse.MetaData.Plan.Itineraries[] itineraries = plan.getItineraries();
        Assertions.assertThat(itineraries.length).isEqualTo(2);

        /*
        Check Plan.Itineraries
         */
        final Long[][] actualFares = {{
                777L,
                5124L,
                972L,
                1L,
                32612L

        }, {
                1257L,
                6053L,
                1346L,
                3L,
                28061L
        }};
        for (int i = 0; i < 2; i++) {
            TMapTransitRoutesSubRawResponse.MetaData.Plan.Itineraries itinerary = itineraries[i];
            Assertions.assertThat(itinerary)
                    .hasFieldOrPropertyWithValue("walkDistance", actualFares[i][0])
                    .hasFieldOrPropertyWithValue("totalTime", actualFares[i][1])
                    .hasFieldOrPropertyWithValue("walkTime", actualFares[i][2])
                    .hasFieldOrPropertyWithValue("transferCount", actualFares[i][3])
                    .hasFieldOrPropertyWithValue("totalDistance", actualFares[i][4]);

            /*
            Check Plan.Itineraries.Fare
             */
            TMapTransitRoutesSubRawResponse.MetaData.Plan.Itineraries.Fare fare = itinerary.getFare();
            Assertions.assertThat(fare).isNotNull();

            /*
            Check Plan.Itineraries.Fare.Regular
             */
            TMapTransitRoutesSubRawResponse.MetaData.Plan.Itineraries.Fare.Regular regular = fare.getRegular();
            TMapTransitRoutesSubRawResponse.MetaData.Plan.Itineraries.Fare.Regular.Currency currency = regular.getCurrency();
            Assertions.assertThat(currency)
                    .hasFieldOrPropertyWithValue("currencyCode", "KRW");
        }


    }


}