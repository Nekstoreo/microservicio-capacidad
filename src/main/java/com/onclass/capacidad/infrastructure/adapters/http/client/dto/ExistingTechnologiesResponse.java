package com.onclass.capacidad.infrastructure.adapters.http.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ExistingTechnologiesResponse(
        @JsonProperty("existingIds") List<Long> existingIds
) {
}
