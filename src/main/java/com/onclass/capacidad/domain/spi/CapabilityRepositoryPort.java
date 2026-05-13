package com.onclass.capacidad.domain.spi;

import com.onclass.capacidad.domain.model.Capability;
import com.onclass.capacidad.domain.models.pagination.DomainPage;
import com.onclass.capacidad.domain.models.pagination.DomainPageRequest;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface CapabilityRepositoryPort {

    boolean existsByName(String name);

    Capability save(Capability capability);

    Capability findById(Long id);

    DomainPage<Capability> findAll(DomainPageRequest pageRequest);

    List<Capability> findAllByIds(List<Long> ids);

    void deleteById(Long id);

    Set<Long> findTechnologyReferences(Collection<Long> technologyIds);
}
