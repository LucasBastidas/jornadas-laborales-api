package com.neoris.turnosrotativos.exceptions;

import org.springframework.http.HttpStatus;

public class EmpleadoBusinessException extends RuntimeException {
    private final HttpStatus status;

    //Se le pasa el mensaje y el status del response
    public EmpleadoBusinessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus(){
        return status;
    }

}


