package com.onclass.capacidad.domain.usecases;

import com.onclass.capacidad.application.dtos.CapabilityWithTechnologies;
import com.onclass.capacidad.domain.api.ListCapabilitiesServicePort;
import com.onclass.capacidad.domain.model.Capability;
import com.onclass.capacidad.domain.models.pagination.DomainPage;
import com.onclass.capacidad.domain.models.pagination.DomainPageRequest;
import com.onclass.capacidad.domain.spi.CapabilityRepositoryPort;
import com.onclass.capacidad.domain.spi.TechnologyCatalogPort;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ListCapabilitiesUseCase implements ListCapabilitiesServicePort {

    private final CapabilityRepositoryPort capabilityRepositoryPort;
    private final TechnologyCatalogPort technologyCatalogPort;

    public ListCapabilitiesUseCase(CapabilityRepositoryPort capabilityRepositoryPort,
                                   TechnologyCatalogPort technologyCatalogPort) {
        this.capabilityRepositoryPort = capabilityRepositoryPort;
        this.technologyCatalogPort = technologyCatalogPort;
    }

    @Override
    public DomainPage<Capability> execute(DomainPageRequest pageRequest) {
        return capabilityRepositoryPort.findAll(pageRequest);
    }

    public DomainPage<CapabilityWithTechnologies> executeWithTechnologyNames(DomainPageRequest pageRequest) {
        DomainPage<Capability> page = capabilityRepositoryPort.findAll(pageRequest);
        Map<Long, String> technologyNames = fetchTechnologyNames(page.content());
        List<CapabilityWithTechnologies> mappedContent = page.content().stream()
                .map(capability -> toDtoWithNames(capability, technologyNames))
                .toList();
        return new DomainPage<>(
                mappedContent,
                page.pageNumber(),
                page.pageSize(),
                page.totalElements(),
                page.totalPages(),
                page.hasNext(),
                page.hasPrevious()
        );
    }

    public List<CapabilityWithTechnologies> getByIdsWithTechnologyNames(List<Long> ids) {
        List<Capability> capabilities = capabilityRepositoryPort.findAllByIds(ids);
        Map<Long, String> technologyNames = fetchTechnologyNames(capabilities);
        return capabilities.stream()
                .map(capability -> toDtoWithNames(capability, technologyNames))
                .toList();
    }

    public CapabilityWithTechnologies getByIdWithTechnologyNames(Long id) {
        Capability capability = capabilityRepositoryPort.findById(id);
        if (capability == null) {
            return null;
        }
        Map<Long, String> technologyNames = fetchTechnologyNames(List.of(capability));
        return toDtoWithNames(capability, technologyNames);
    }

    private Map<Long, String> fetchTechnologyNames(Collection<Capability> capabilities) {
        Set<Long> technologyIds = capabilities.stream()
                .flatMap(c -> c.getTechnologyIds().stream())
                .collect(Collectors.toSet());

        return technologyIds.isEmpty()
                ? Map.of()
                : technologyCatalogPort.findTechnologiesByIds(technologyIds);
    }

    private CapabilityWithTechnologies toDtoWithNames(Capability capability, Map<Long, String> names) {
        List<CapabilityWithTechnologies.TechnologyInfo> techInfos = capability.getTechnologyIds().stream()
                .map(id -> new CapabilityWithTechnologies.TechnologyInfo(id, names.get(id)))
                .toList();

        return new CapabilityWithTechnologies(
                capability.getId(),
                capability.getName(),
                capability.getDescription(),
                techInfos
        );
    }
}
