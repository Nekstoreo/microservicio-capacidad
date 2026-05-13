package com.onclass.capacidad.domain.usecases;

import com.onclass.capacidad.domain.api.DeleteCapabilityServicePort;
import com.onclass.capacidad.domain.exception.CapabilityNotFoundException;
import com.onclass.capacidad.domain.model.Capability;
import com.onclass.capacidad.domain.spi.CapabilityRepositoryPort;
import com.onclass.capacidad.domain.spi.TechnologyManagementPort;

import java.util.List;
import java.util.Set;

public class DeleteCapabilityUseCase implements DeleteCapabilityServicePort {

    private final CapabilityRepositoryPort capabilityRepositoryPort;
    private final TechnologyManagementPort technologyManagementPort;

    public DeleteCapabilityUseCase(CapabilityRepositoryPort capabilityRepositoryPort,
                                   TechnologyManagementPort technologyManagementPort) {
        this.capabilityRepositoryPort = capabilityRepositoryPort;
        this.technologyManagementPort = technologyManagementPort;
    }

    @Override
    public void execute(Long capabilityId) {
        Capability capability = capabilityRepositoryPort.findById(capabilityId);
        if (capability == null) {
            throw new CapabilityNotFoundException(capabilityId);
        }

        List<Long> technologyIds = capability.getTechnologyIds();
        capabilityRepositoryPort.deleteById(capabilityId);

        Set<Long> referencedTechnologyIds = capabilityRepositoryPort.findTechnologyReferences(technologyIds);
        technologyIds.stream()
                .filter(id -> !referencedTechnologyIds.contains(id))
                .forEach(technologyManagementPort::deleteTechnology);
    }
}
