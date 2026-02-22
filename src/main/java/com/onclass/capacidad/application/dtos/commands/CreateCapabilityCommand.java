package com.onclass.capacidad.application.dtos.commands;

import java.util.List;

public record CreateCapabilityCommand(
        String name,
        String description,
        List<Long> technologyIds
) {
}
