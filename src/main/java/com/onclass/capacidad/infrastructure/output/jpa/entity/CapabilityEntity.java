package com.onclass.capacidad.infrastructure.output.jpa.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "capabilities")
public class CapabilityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 150)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @OneToMany(mappedBy = "capability", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CapabilityTechnologyEntity> technologies = new ArrayList<>();

    public void addTechnology(CapabilityTechnologyEntity capabilityTechnologyEntity) {
        technologies.add(capabilityTechnologyEntity);
        capabilityTechnologyEntity.setCapability(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CapabilityTechnologyEntity> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(List<CapabilityTechnologyEntity> technologies) {
        this.technologies = technologies;
    }
}
