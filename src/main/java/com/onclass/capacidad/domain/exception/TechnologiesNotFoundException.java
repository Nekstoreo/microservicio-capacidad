package com.onclass.capacidad.domain.exception;

import com.onclass.capacidad.domain.constants.DomainConstants;

import java.util.List;

public class TechnologiesNotFoundException extends RuntimeException {

    public TechnologiesNotFoundException(List<Long> missingIds) {
        super(DomainConstants.technologiesNotFoundMessage(missingIds));
    }
}
