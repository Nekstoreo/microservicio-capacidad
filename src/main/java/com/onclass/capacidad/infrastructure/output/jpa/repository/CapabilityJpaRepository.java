package com.onclass.capacidad.infrastructure.output.jpa.repository;

import com.onclass.capacidad.infrastructure.output.jpa.entity.CapabilityEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CapabilityJpaRepository extends JpaRepository<CapabilityEntity, Long> {

    boolean existsByName(String name);

    @Query("SELECT c FROM CapabilityEntity c ORDER BY c.name ASC")
    Page<CapabilityEntity> findAllOrderByNameAsc(Pageable pageable);

    @Query("SELECT c FROM CapabilityEntity c ORDER BY c.name DESC")
    Page<CapabilityEntity> findAllOrderByNameDesc(Pageable pageable);

    @Query("SELECT c FROM CapabilityEntity c LEFT JOIN c.technologies t GROUP BY c.id ORDER BY COUNT(t) ASC, c.id ASC")
    Page<CapabilityEntity> findAllOrderByTechnologyCountAsc(Pageable pageable);

    @Query("SELECT c FROM CapabilityEntity c LEFT JOIN c.technologies t GROUP BY c.id ORDER BY COUNT(t) DESC, c.id ASC")
    Page<CapabilityEntity> findAllOrderByTechnologyCountDesc(Pageable pageable);
}
