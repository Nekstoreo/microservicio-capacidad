package com.onclass.capacidad.domain.api;

import com.onclass.capacidad.domain.model.Capability;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ListCapabilitiesServicePort {

    Page<Capability> execute(Pageable pageable);
}
