package com.onclass.capacidad.infrastructure.output.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

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

    @Column(name = "technology_name", nullable = false, length = 50)
    private String technologyName;

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

    public String getTechnologyName() {
        return technologyName;
    }

    public void setTechnologyName(String technologyName) {
        this.technologyName = technologyName;
    }
}

