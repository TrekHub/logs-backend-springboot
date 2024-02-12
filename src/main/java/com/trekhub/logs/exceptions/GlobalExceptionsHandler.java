package com.trekhub.logs.exceptions;

import jakarta.persistence.EntityExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

//TODO: Check out AOP aspect oriented programming
@ControllerAdvice
public class GlobalExceptionsHandler extends ResponseEntityExceptionHandler {


     @ExceptionHandler(value = {EntityExistsException.class})
    public ResponseEntity<ExceptionObject> handleEntityExistsException(EntityExistsException entityExistsException){
        ExceptionObject exceptionObject = new ExceptionObject(HttpStatus.CONFLICT.value(), entityExistsException.getMessage() );
        return  new ResponseEntity<>(exceptionObject, HttpStatus.CONFLICT);
    }

}
