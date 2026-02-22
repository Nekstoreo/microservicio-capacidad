package com.onclass.capacidad.application.dtos;

import java.util.List;

public record CapabilityWithTechnologies(
        Long id,
        String name,
        String description,
        List<TechnologyInfo> technologies
) {

    public record TechnologyInfo(
            Long id,
            String name
    ) {
    }
}
