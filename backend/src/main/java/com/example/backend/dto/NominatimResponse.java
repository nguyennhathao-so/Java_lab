package com.example.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NominatimResponse {
    @JsonProperty("place_id")
    private Long placeId;
    
    private String licence;
    
    @JsonProperty("osm_type")
    private String osmType;
    
    @JsonProperty("osm_id")
    private Long osmId;
    
    private String lat;
    private String lon;
    
    @JsonProperty("display_name")
    private String displayName;
    
    private Address address;
    
    @JsonProperty("boundingbox")
    private String[] boundingbox;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Address {
        private String house_number;
        private String road;
        private String suburb;
        private String city;
        private String state;
        private String postcode;
        private String country;
        @JsonProperty("country_code")
        private String countryCode;
    }
} 