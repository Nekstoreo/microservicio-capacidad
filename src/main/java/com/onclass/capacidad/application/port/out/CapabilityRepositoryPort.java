package com.onclass.capacidad.application.port.out;

import com.onclass.capacidad.application.dto.CapabilityWithTechnologies;
import com.onclass.capacidad.domain.model.Capability;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CapabilityRepositoryPort {

    boolean existsByName(String name);

    Capability save(Capability capability);

    Capability findById(Long id);

    Page<Capability> findAll(Pageable pageable);

    Page<CapabilityWithTechnologies> findAllWithTechnologyNames(Pageable pageable);
}
