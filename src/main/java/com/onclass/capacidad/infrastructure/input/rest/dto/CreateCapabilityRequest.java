package com.onclass.capacidad.infrastructure.input.rest.dto;

import com.onclass.capacidad.infrastructure.constants.ApiConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateCapabilityRequest(
        @NotBlank(message = ApiConstants.VALIDATION_NAME_REQUIRED)
        String name,

        @NotBlank(message = ApiConstants.VALIDATION_DESCRIPTION_REQUIRED)
        String description,

        @NotEmpty(message = ApiConstants.VALIDATION_TECHNOLOGY_IDS_REQUIRED)
        @Size(min = 3, max = 20, message = ApiConstants.VALIDATION_TECHNOLOGIES_SIZE)
        List<@NotNull(message = ApiConstants.VALIDATION_TECHNOLOGY_ID_REQUIRED) Long> technologyIds
) {
}
