package com.onclass.capacidad.infrastructure.output.http;

import com.onclass.capacidad.application.exception.TechnologyCatalogUnavailableException;
import com.onclass.capacidad.application.port.out.TechnologyCatalogPort;
import com.onclass.capacidad.infrastructure.constants.ApiConstants;
import com.onclass.capacidad.infrastructure.output.http.dto.ExistingTechnologiesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TechnologyWebClientAdapter implements TechnologyCatalogPort {

    private final WebClient technologyWebClient;

    @Override
    public Set<Long> findExistingTechnologyIds(Set<Long> technologyIds) {
        String idsParam = technologyIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        try {
            ExistingTechnologiesResponse response = technologyWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(ApiConstants.TECHNOLOGY_EXISTS_PATH)
                            .queryParam(ApiConstants.TECHNOLOGY_EXISTS_QUERY_PARAM, idsParam)
                            .build())
                    .retrieve()
                    .bodyToMono(ExistingTechnologiesResponse.class)
                    .block();

            if (response == null || response.existingIds() == null) {
                return Collections.emptySet();
            }

            return new HashSet<>(response.existingIds());
        } catch (WebClientRequestException | WebClientResponseException _) {
            throw new TechnologyCatalogUnavailableException(ApiConstants.TECHNOLOGY_CATALOG_UNAVAILABLE_MESSAGE);
        }
    }
}
