package com.example.backend.service;

import com.example.backend.dto.NominatimResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GeocodingService {
    
    private final WebClient webClient;
    private static final String NOMINATIM_BASE_URL = "https://nominatim.openstreetmap.org";
    
    @Autowired
    public GeocodingService(WebClient webClient) {
        this.webClient = webClient.mutate()
                .baseUrl(NOMINATIM_BASE_URL)
                .build();
    }
    
    /**
     * Test URL geocoding và trả về dữ liệu thực tế
     * @param address Địa chỉ cần test
     * @return Dữ liệu JSON từ Nominatim API
     */
    public String testGeocodingUrl(String address) {
        if (address == null || address.trim().isEmpty()) {
            return "Error: Address is empty";
        }
        
        try {
            // Build URI để log ra URL thực tế (đã encode)
            String url = org.springframework.web.util.UriComponentsBuilder.fromHttpUrl(NOMINATIM_BASE_URL)
                    .path("/search")
                    .queryParam("q", address)
                    .queryParam("format", "json")
                    .queryParam("limit", "1")
                    .queryParam("addressdetails", "1")
                    .build()
                    .encode()
                    .toUriString();
            
            System.out.println("Testing Geocoding URL: " + url);
            
            // Gọi API và lấy response dạng String để xem dữ liệu thực tế
            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search")
                            .queryParam("q", address)
                            .queryParam("format", "json")
                            .queryParam("limit", "1")
                            .queryParam("addressdetails", "1")
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(java.time.Duration.ofSeconds(10))
                    .block();
            
            if (response != null && !response.isEmpty()) {
                System.out.println("Response from Nominatim: " + response);
                return "URL: " + url + "\n\nResponse: " + response;
            } else {
                return "URL: " + url + "\n\nResponse: Empty response from API";
            }
            
        } catch (Exception e) {
            String errorMsg = "Error testing geocoding URL for address: " + address + " - " + e.getMessage();
            System.err.println(errorMsg);
            return "Error: " + errorMsg;
        }
    }
    
    /**
     * Chuyển đổi địa chỉ thành tọa độ sử dụng Nominatim API
     * @param address Địa chỉ cần chuyển đổi
     * @return Tọa độ dạng "latitude,longitude" hoặc null nếu không tìm thấy
     */
    public String geocodeAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            return null;
        }
        
        try {
            List<NominatimResponse> responses = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search")
                            .queryParam("q", address)
                            .queryParam("format", "json")
                            .queryParam("limit", "1")
                            .queryParam("addressdetails", "1")
                            .build())
                    .retrieve()
                    .bodyToMono(new org.springframework.core.ParameterizedTypeReference<List<NominatimResponse>>() {})
                    .block();
            
            if (responses != null && !responses.isEmpty()) {
                NominatimResponse response = responses.get(0);
                if (response.getLat() != null && response.getLon() != null) {
                    return response.getLat() + "," + response.getLon();
                }
            }
            
            return null;
        } catch (Exception e) {
            // Log error but don't throw exception to avoid breaking registration
            System.err.println("Error geocoding address: " + address + " - " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Chuyển đổi địa chỉ thành tọa độ với timeout
     * @param address Địa chỉ cần chuyển đổi
     * @return Tọa độ dạng "latitude,longitude" hoặc null nếu không tìm thấy
     */
    public String geocodeAddressWithTimeout(String address) {
        if (address == null || address.trim().isEmpty()) {
            return null;
        }
        
        try {
            // Build URI để log ra URL thực tế (đã encode)
            String url = org.springframework.web.util.UriComponentsBuilder.fromHttpUrl(NOMINATIM_BASE_URL)
                    .path("/search")
                    .queryParam("q", address)
                    .queryParam("format", "json")
                    .queryParam("limit", "1")
                    .queryParam("addressdetails", "1")
                    .build()
                    .encode()
                    .toUriString();
            System.out.println("Geocoding URL: " + url);

            List<NominatimResponse> responses = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search")
                            .queryParam("q", address)
                            .queryParam("format", "json")
                            .queryParam("limit", "1")
                            .queryParam("addressdetails", "1")
                            .build())
                    .retrieve()
                    .bodyToMono(new org.springframework.core.ParameterizedTypeReference<List<NominatimResponse>>() {})
                    .timeout(java.time.Duration.ofSeconds(10))
                    .block();
            
            if (responses != null && !responses.isEmpty()) {
                NominatimResponse response = responses.get(0);
                if (response.getLat() != null && response.getLon() != null) {
                    return response.getLat() + "," + response.getLon();
                }
            }
            
            return null;
        } catch (Exception e) {
            // Log error but don't throw exception to avoid breaking registration
            System.err.println("Error geocoding address: " + address + " - " + e.getMessage());
            return null;
        }
    }
} 