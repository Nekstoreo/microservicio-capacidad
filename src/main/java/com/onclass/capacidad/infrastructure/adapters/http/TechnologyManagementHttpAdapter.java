package com.onclass.capacidad.infrastructure.adapters.http;

import com.onclass.capacidad.application.exception.TechnologyDeletionException;
import com.onclass.capacidad.domain.spi.TechnologyManagementPort;
import com.onclass.capacidad.infrastructure.constants.ApiConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@RequiredArgsConstructor
public class TechnologyManagementHttpAdapter implements TechnologyManagementPort {

    private final WebClient technologyWebClient;

    @Override
    public void deleteTechnology(Long technologyId) {
        try {
            technologyWebClient.delete()
                    .uri(ApiConstants.TECHNOLOGIES_BASE_PATH + "/{id}", technologyId)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (WebClientResponseException e) {
            throw new TechnologyDeletionException(
                    "Failed to delete technology " + technologyId + ": " + e.getMessage());
        }
    }
}
