package com.onclass.capacidad.domain.models.pagination;

public record DomainPageRequest(
        int page,
        int size,
        String sortBy,
        String sortDirection
) {
}
