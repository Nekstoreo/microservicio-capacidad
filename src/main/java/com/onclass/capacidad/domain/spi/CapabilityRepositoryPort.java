package com.onclass.capacidad.domain.spi;

import com.onclass.capacidad.domain.model.Capability;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface CapabilityRepositoryPort {

    boolean existsByName(String name);

    Capability save(Capability capability);

    Capability findById(Long id);

    Page<Capability> findAll(Pageable pageable);

    List<Capability> findAllByIds(List<Long> ids);

    void deleteById(Long id);

    Set<Long> findTechnologyReferences(Collection<Long> technologyIds);
}
