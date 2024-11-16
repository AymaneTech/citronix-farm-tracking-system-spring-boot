package com.wora.citronix.common.domain.exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String entityName, Object id) {
        super(entityName + " with id " + id + " not found");
    }

    public EntityNotFoundException(String message) {
        super(message);
    }
}
