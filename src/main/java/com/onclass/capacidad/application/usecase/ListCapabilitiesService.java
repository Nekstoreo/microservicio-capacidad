package com.onclass.capacidad.application.usecase;

import com.onclass.capacidad.application.dto.CapabilityWithTechnologies;
import com.onclass.capacidad.application.port.in.ListCapabilitiesUseCase;
import com.onclass.capacidad.application.port.out.CapabilityRepositoryPort;
import com.onclass.capacidad.application.port.out.TechnologyCatalogPort;
import com.onclass.capacidad.domain.model.Capability;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListCapabilitiesService implements ListCapabilitiesUseCase {

    private final CapabilityRepositoryPort capabilityRepositoryPort;
    private final TechnologyCatalogPort technologyCatalogPort;

    @Override
    public Page<Capability> execute(Pageable pageable) {
        return capabilityRepositoryPort.findAll(pageable);
    }

    public Page<CapabilityWithTechnologies> executeWithTechnologyNames(Pageable pageable) {
        Page<Capability> page = capabilityRepositoryPort.findAll(pageable);
        Map<Long, String> technologyNames = fetchTechnologyNames(page.getContent());
        return page.map(capability -> toDtoWithNames(capability, technologyNames));
    }

    public List<CapabilityWithTechnologies> getByIdsWithTechnologyNames(List<Long> ids) {
        List<Capability> capabilities = capabilityRepositoryPort.findAllByIds(ids);
        Map<Long, String> technologyNames = fetchTechnologyNames(capabilities);
        return capabilities.stream()
                .map(capability -> toDtoWithNames(capability, technologyNames))
                .toList();
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
