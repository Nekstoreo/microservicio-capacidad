package com.onclass.capacidad.domain.exception;

import com.onclass.capacidad.domain.constants.DomainConstants;

public class DuplicateCapabilityException extends RuntimeException {

    public DuplicateCapabilityException(String name) {
        super(DomainConstants.duplicateCapabilityMessage(name));
    }
}
