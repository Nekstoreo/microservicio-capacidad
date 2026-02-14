package com.onclass.capacidad.infrastructure.input.rest.dto;

import com.onclass.capacidad.domain.model.Capability;

import java.util.List;

public record CapabilityResponse(
        Long id,
        String name,
        String description,
        List<Long> technologyIds
) {

    public static CapabilityResponse fromDomain(Capability capability) {
        return new CapabilityResponse(
                capability.getId(),
                capability.getName(),
                capability.getDescription(),
                capability.getTechnologyIds()
        );
    }
}
