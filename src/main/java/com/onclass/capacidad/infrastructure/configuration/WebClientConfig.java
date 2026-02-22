package com.onclass.capacidad.infrastructure.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final ApplicationProperties applicationProperties;

    @Bean
    public WebClient technologyWebClient() {
        String credentials = applicationProperties.getHttpClient().getCredentials().getUsername() + ":" 
                           + applicationProperties.getHttpClient().getCredentials().getPassword();
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        
        return WebClient.builder()
                .baseUrl(applicationProperties.getTechnology().getBaseUrl())
                .defaultHeader("Authorization", "Basic " + encodedCredentials)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024))
                        .build())
                .build();
    }
}


