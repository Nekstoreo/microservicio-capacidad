package com.onclass.capacidad.application.port.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.onclass.capacidad.domain.model.Capability;

public interface ListCapabilitiesUseCase {

    Page<Capability> execute(Pageable pageable);
}
