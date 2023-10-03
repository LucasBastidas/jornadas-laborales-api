package com.neoris.turnosrotativos.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(EmpleadoBusinessException.class)
    public ResponseEntity<Object> handleEmpleadoBusinessException(EmpleadoBusinessException ex) {
        //Se utiliza el status y el message que llega y devuelve el response
        HttpStatus status = ex.getStatus();
        String errorMessage = ex.getMessage();
        return new ResponseEntity<>(errorMessage, status);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)//HANDLER PARA VALIDAR LOS CAMPOS OBLIGATORIOS
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex){
        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        //SE CREA UN MAP DE ERRORES Y MENSAJES
        Map<String, String> errors = new HashMap<>();

        for (FieldError fieldError : fieldErrors){
            String fieldName = fieldError.getField();
            String errorMessage = fieldError.getDefaultMessage();
            //Se agregan todos los errores y sus respectivos mensajes al Map
            errors.put(fieldName,errorMessage);
        }
        HttpStatus status = HttpStatus.BAD_REQUEST;
        //SE DEVUELVE EL MAP CON LOS ERRORES Y MENSAJES Y EL STATUS "BAD REQUEST"
        return new ResponseEntity<>(errors,status);
    }
}
