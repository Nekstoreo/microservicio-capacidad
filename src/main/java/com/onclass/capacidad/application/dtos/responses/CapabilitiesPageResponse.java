package com.onclass.capacidad.application.dtos.responses;

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
