package com.onclass.capacidad.domain.exception;

import com.onclass.capacidad.domain.constants.DomainConstants;

public class CapabilityNotFoundException extends RuntimeException {

    public CapabilityNotFoundException(Long capabilityId) {
        super(String.format(DomainConstants.CAPABILITY_NOT_FOUND_MESSAGE, capabilityId));
    }
}