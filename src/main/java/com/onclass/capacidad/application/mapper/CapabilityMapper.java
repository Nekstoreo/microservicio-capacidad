package com.onclass.capacidad.application.mapper;

import com.onclass.capacidad.application.dto.CapabilityWithTechnologies;
import com.onclass.capacidad.infrastructure.input.rest.dto.CapabilityListItemResponse;
import com.onclass.capacidad.infrastructure.input.rest.dto.TechnologyRefResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CapabilityMapper {

    public CapabilityListItemResponse toListItemResponse(CapabilityWithTechnologies capability) {
        List<TechnologyRefResponse> techResponses = capability.technologies().stream()
                .map(tech -> new TechnologyRefResponse(tech.id(), tech.name()))
                .toList();

        return new CapabilityListItemResponse(
                capability.id(),
                capability.name(),
                capability.description(),
                techResponses
        );
    }
}
