package com.onclass.capacidad.domain.api;

import com.onclass.capacidad.application.dtos.CapabilityWithTechnologies;
import com.onclass.capacidad.domain.model.Capability;
import com.onclass.capacidad.domain.models.pagination.DomainPage;
import com.onclass.capacidad.domain.models.pagination.DomainPageRequest;

import java.util.List;

public interface ListCapabilitiesServicePort {

    DomainPage<Capability> execute(DomainPageRequest pageRequest);

    DomainPage<CapabilityWithTechnologies> executeWithTechnologyNames(DomainPageRequest pageRequest);

    List<CapabilityWithTechnologies> getByIdsWithTechnologyNames(List<Long> ids);

    CapabilityWithTechnologies getByIdWithTechnologyNames(Long id);
}
