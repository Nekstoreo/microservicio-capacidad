package com.onclass.capacidad.application.port.out;

import java.util.Map;
import java.util.Set;

public interface TechnologyCatalogPort {

    Set<Long> findExistingTechnologyIds(Set<Long> technologyIds);

    Map<Long, String> findTechnologiesByIds(Set<Long> technologyIds);
}
