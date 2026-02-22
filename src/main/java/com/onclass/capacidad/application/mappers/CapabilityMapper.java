package com.onclass.capacidad.application.mappers;

import com.onclass.capacidad.application.dtos.CapabilityWithTechnologies;
import com.onclass.capacidad.application.dtos.responses.CapabilityListItemResponse;
import com.onclass.capacidad.application.dtos.responses.TechnologyRefResponse;
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
