package com.onclass.capacidad.application.dtos.responses;

import java.util.List;

public record CapabilityListItemResponse(
        Long id,
        String name,
        String description,
        List<TechnologyRefResponse> technologies
) {

}
