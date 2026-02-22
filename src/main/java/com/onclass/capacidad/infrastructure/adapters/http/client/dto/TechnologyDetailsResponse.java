package com.onclass.capacidad.infrastructure.adapters.http.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TechnologyDetailsResponse(
        @JsonProperty("technologies") List<TechnologyDetail> technologies
) {
    public record TechnologyDetail(Long id, String name) {
    }
}
