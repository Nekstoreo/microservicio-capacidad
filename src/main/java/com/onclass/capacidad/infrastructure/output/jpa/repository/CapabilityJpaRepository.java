package com.onclass.capacidad.infrastructure.output.jpa.repository;

import com.onclass.capacidad.infrastructure.output.jpa.entity.CapabilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CapabilityJpaRepository extends JpaRepository<CapabilityEntity, Long> {

    boolean existsByName(String name);
}
