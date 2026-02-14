package com.onclass.capacidad.domain.constants;

import java.util.List;

public final class DomainConstants {

    public static final int CAPABILITY_MIN_TECHNOLOGIES = 3;
    public static final int CAPABILITY_MAX_TECHNOLOGIES = 20;

    public static final String CAPABILITY_NAME_REQUIRED_MESSAGE = "Capability name is required";
    public static final String CAPABILITY_DESCRIPTION_REQUIRED_MESSAGE = "Capability description is required";
    public static final String CAPABILITY_TECHNOLOGIES_REQUIRED_MESSAGE = "Capability must include technologies";
    public static final String CAPABILITY_TECHNOLOGIES_MIN_MESSAGE = "Capability must include at least 3 technologies";
    public static final String CAPABILITY_TECHNOLOGIES_MAX_MESSAGE = "Capability must include at most 20 technologies";
    public static final String CAPABILITY_TECHNOLOGIES_DUPLICATED_MESSAGE = "Capability technologies cannot contain duplicates";
    public static final String CAPABILITY_TECHNOLOGY_ID_INVALID_MESSAGE = "Technology id must be a positive number";

    private static final String DUPLICATE_CAPABILITY_TEMPLATE = "A capability with name already exists: %s";
    private static final String TECHNOLOGIES_NOT_FOUND_TEMPLATE = "Technologies not found: %s";

    private DomainConstants() {
    }

    public static String duplicateCapabilityMessage(String name) {
        return DUPLICATE_CAPABILITY_TEMPLATE.formatted(name);
    }

    public static String technologiesNotFoundMessage(List<Long> missingIds) {
        return TECHNOLOGIES_NOT_FOUND_TEMPLATE.formatted(missingIds);
    }
}
