package com.onclass.capacidad.infrastructure.output.http.dto;

import java.util.List;

public record TechnologyDetailsResponse(
        List<TechnologyDetail> technologies
) {
    public record TechnologyDetail(
            Long id,
            String name
    ) {
    }
}
