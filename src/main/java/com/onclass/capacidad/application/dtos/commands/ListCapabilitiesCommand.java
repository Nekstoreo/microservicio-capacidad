package com.onclass.capacidad.application.dtos.commands;

public record ListCapabilitiesCommand(
        Integer page,
        Integer size,
        String sort
) {
}
