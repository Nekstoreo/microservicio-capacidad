package com.onclass.capacidad.application.usecase;

import com.onclass.capacidad.application.dto.CapabilityWithTechnologies;
import com.onclass.capacidad.application.port.in.ListCapabilitiesUseCase;
import com.onclass.capacidad.application.port.out.CapabilityRepositoryPort;
import com.onclass.capacidad.domain.model.Capability;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListCapabilitiesService implements ListCapabilitiesUseCase {

    private final CapabilityRepositoryPort capabilityRepositoryPort;

    @Override
    public Page<Capability> execute(Pageable pageable) {
        return capabilityRepositoryPort.findAll(pageable);
    }

    public Page<CapabilityWithTechnologies> executeWithTechnologyNames(Pageable pageable) {
        return capabilityRepositoryPort.findAllWithTechnologyNames(pageable);
    }
}
