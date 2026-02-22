package com.onclass.capacidad.domain.api;

import com.onclass.capacidad.application.dtos.commands.CreateCapabilityCommand;
import com.onclass.capacidad.domain.model.Capability;

public interface CreateCapabilityServicePort {

    Capability execute(CreateCapabilityCommand command);
}
