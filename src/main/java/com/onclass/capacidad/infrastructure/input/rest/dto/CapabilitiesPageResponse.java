package com.onclass.capacidad.infrastructure.input.rest.dto;

import java.util.List;

public record CapabilitiesPageResponse(
        List<CapabilityListItemResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
) {

}
