package com.onclass.capacidad.application.usecase.command;

import java.util.List;

public record CreateCapabilityCommand(
        String name,
        String description,
        List<Long> technologyIds
) {
}
