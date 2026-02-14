package com.onclass.capacidad.infrastructure.input.rest.dto;

import java.util.List;

public record CapabilityListItemResponse(
        Long id,
        String name,
        String description,
        List<TechnologyRefResponse> technologies
) {

}
