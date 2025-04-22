package com.mycompany.ecommercecustomer.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InternalServerErrorAlertException extends ResponseStatusException {

    public InternalServerErrorAlertException(String message, String entityName, String detailMessage) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, String.format("%s. Entity: %s. Detail: %s", message, entityName, detailMessage));
    }
}
