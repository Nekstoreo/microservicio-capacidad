package com.onclass.capacidad.domain.model;

import com.onclass.capacidad.domain.constants.DomainConstants;
import com.onclass.capacidad.domain.exception.DomainValidationException;

import java.util.LinkedHashSet;
import java.util.List;

public class Capability {

    private final Long id;
    private final String name;
    private final String description;
    private final List<Long> technologyIds;

    private Capability(Long id, String name, String description, List<Long> technologyIds) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.technologyIds = List.copyOf(technologyIds);
    }

    public static Capability create(String name, String description, List<Long> technologyIds) {
        String normalizedName = normalize(name);
        String normalizedDescription = normalize(description);
        validateName(normalizedName);
        validateDescription(normalizedDescription);
        List<Long> validatedTechnologyIds = validateTechnologyIds(technologyIds);
        return new Capability(null, normalizedName, normalizedDescription, validatedTechnologyIds);
    }

    public static Capability rehydrate(Long id, String name, String description, List<Long> technologyIds) {
        String normalizedName = normalize(name);
        String normalizedDescription = normalize(description);
        validateName(normalizedName);
        validateDescription(normalizedDescription);
        List<Long> validatedTechnologyIds = validateTechnologyIds(technologyIds);
        return new Capability(id, normalizedName, normalizedDescription, validatedTechnologyIds);
    }

    private static String normalize(String value) {
        return value == null ? null : value.trim();
    }

    private static void validateName(String value) {
        if (value == null || value.isBlank()) {
            throw new DomainValidationException(DomainConstants.CAPABILITY_NAME_REQUIRED_MESSAGE);
        }
    }

    private static void validateDescription(String value) {
        if (value == null || value.isBlank()) {
            throw new DomainValidationException(DomainConstants.CAPABILITY_DESCRIPTION_REQUIRED_MESSAGE);
        }
    }

    private static List<Long> validateTechnologyIds(List<Long> technologyIds) {
        if (technologyIds == null || technologyIds.isEmpty()) {
            throw new DomainValidationException(DomainConstants.CAPABILITY_TECHNOLOGIES_REQUIRED_MESSAGE);
        }
        if (technologyIds.size() < DomainConstants.CAPABILITY_MIN_TECHNOLOGIES) {
            throw new DomainValidationException(DomainConstants.CAPABILITY_TECHNOLOGIES_MIN_MESSAGE);
        }
        if (technologyIds.size() > DomainConstants.CAPABILITY_MAX_TECHNOLOGIES) {
            throw new DomainValidationException(DomainConstants.CAPABILITY_TECHNOLOGIES_MAX_MESSAGE);
        }

        LinkedHashSet<Long> uniqueTechnologyIds = new LinkedHashSet<>();
        for (Long technologyId : technologyIds) {
            if (technologyId == null || technologyId <= 0) {
                throw new DomainValidationException(DomainConstants.CAPABILITY_TECHNOLOGY_ID_INVALID_MESSAGE);
            }
            uniqueTechnologyIds.add(technologyId);
        }

        if (uniqueTechnologyIds.size() != technologyIds.size()) {
            throw new DomainValidationException(DomainConstants.CAPABILITY_TECHNOLOGIES_DUPLICATED_MESSAGE);
        }

        return List.copyOf(technologyIds);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Long> getTechnologyIds() {
        return technologyIds;
    }
}
