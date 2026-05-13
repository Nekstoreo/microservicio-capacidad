package com.onclass.capacidad.domain.models.pagination;

import java.util.List;

public record DomainPage<T>(
        List<T> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
) {
}
