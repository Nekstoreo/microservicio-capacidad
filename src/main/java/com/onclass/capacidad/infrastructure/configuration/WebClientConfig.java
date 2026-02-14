package com.onclass.capacidad.infrastructure.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final ApplicationProperties applicationProperties;

    @Bean
    public WebClient technologyWebClient() {
        return WebClient.builder()
                .baseUrl(applicationProperties.getTechnology().getBaseUrl())
                .build();
    }
}
