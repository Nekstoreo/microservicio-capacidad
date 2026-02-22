package com.onclass.capacidad.infrastructure.adapters.http;

import com.onclass.capacidad.application.exception.TechnologyCatalogUnavailableException;
import com.onclass.capacidad.domain.spi.TechnologyCatalogPort;
import com.onclass.capacidad.infrastructure.adapters.http.client.dto.ExistingTechnologiesResponse;
import com.onclass.capacidad.infrastructure.adapters.http.client.dto.TechnologyDetailsResponse;
import com.onclass.capacidad.infrastructure.constants.ApiConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TechnologyCatalogHttpAdapter implements TechnologyCatalogPort {

    private final WebClient technologyWebClient;

    @Override
    public Set<Long> findExistingTechnologyIds(Set<Long> technologyIds) {
        if (technologyIds == null || technologyIds.isEmpty()) {
            return Set.of();
        }

        String idsParam = technologyIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        try {
            ExistingTechnologiesResponse response = technologyWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(ApiConstants.TECHNOLOGIES_BASE_PATH + "/exists")
                            .queryParam(ApiConstants.TECHNOLOGY_EXISTS_QUERY_PARAM, idsParam)
                            .build())
                    .retrieve()
                    .bodyToMono(ExistingTechnologiesResponse.class)
                    .block();

            return response != null ? Set.copyOf(response.existingIds()) : Set.of();
        } catch (WebClientResponseException _) {
            throw new TechnologyCatalogUnavailableException(ApiConstants.TECHNOLOGY_CATALOG_UNAVAILABLE_MESSAGE);
        }
    }

    @Override
    public Map<Long, String> findTechnologiesByIds(Set<Long> technologyIds) {
        if (technologyIds == null || technologyIds.isEmpty()) {
            return Map.of();
        }

        String idsParam = technologyIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        try {
            TechnologyDetailsResponse response = technologyWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(ApiConstants.TECHNOLOGIES_BASE_PATH + "/details")
                            .queryParam(ApiConstants.TECHNOLOGY_EXISTS_QUERY_PARAM, idsParam)
                            .build())
                    .retrieve()
                    .bodyToMono(TechnologyDetailsResponse.class)
                    .block();

            if (response == null || response.technologies() == null) {
                return Map.of();
            }

            return response.technologies().stream()
                    .filter(t -> t.id() != null && t.name() != null)
                    .collect(Collectors.toMap(TechnologyDetailsResponse.TechnologyDetail::id,
                            TechnologyDetailsResponse.TechnologyDetail::name, (a, b) -> a));
        } catch (WebClientResponseException _) {
            throw new TechnologyCatalogUnavailableException(ApiConstants.TECHNOLOGY_CATALOG_UNAVAILABLE_MESSAGE);
        }
    }
}
