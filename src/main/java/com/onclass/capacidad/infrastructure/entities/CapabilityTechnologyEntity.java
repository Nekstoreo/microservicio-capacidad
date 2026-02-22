package com.onclass.capacidad.infrastructure.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "capability_technologies", uniqueConstraints = {
        @UniqueConstraint(name = "uk_capability_technology", columnNames = {"capability_id", "technology_id"})
})
public class CapabilityTechnologyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "capability_id", nullable = false)
    private CapabilityEntity capability;

    @Column(name = "technology_id", nullable = false)
    private Long technologyId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CapabilityEntity getCapability() {
        return capability;
    }

    public void setCapability(CapabilityEntity capability) {
        this.capability = capability;
    }

    public Long getTechnologyId() {
        return technologyId;
    }

    public void setTechnologyId(Long technologyId) {
        this.technologyId = technologyId;
    }
}

