package com.example.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .defaultHeader("User-Agent", "BloodDonationApp/1.0 (https://github.com/your-repo; contact@example.com)")
                .build();
    }
} 