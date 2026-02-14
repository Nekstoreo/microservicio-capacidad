package com.onclass.capacidad.application.port.out;

import com.onclass.capacidad.domain.model.Capability;

public interface CapabilityRepositoryPort {

    boolean existsByName(String name);

    Capability save(Capability capability);
}
