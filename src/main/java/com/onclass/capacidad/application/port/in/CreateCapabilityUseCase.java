package com.onclass.capacidad.application.port.in;

import com.onclass.capacidad.application.usecase.command.CreateCapabilityCommand;
import com.onclass.capacidad.domain.model.Capability;

public interface CreateCapabilityUseCase {

    Capability execute(CreateCapabilityCommand command);
}
