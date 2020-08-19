package com.spares.dealer.controller;

import com.spares.dealer.DTO.ErrorDTO;
import com.spares.dealer.exception.ProductException;
import com.spares.dealer.exception.ProductNotFoundException;
import com.spares.dealer.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {


    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Object> handleProductNotFoundException(
            ProductNotFoundException ex, WebRequest request) {
        System.out.println(ex);
        ErrorDTO error= new ErrorDTO("Error","Product not found");
        return new ResponseEntity<>(error, HttpStatus.OK);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(
            UserNotFoundException ex, WebRequest request) {
        logger.info(ex);
        ErrorDTO error= new ErrorDTO("Error","Product not Owned By user");
        return new ResponseEntity<>(error, HttpStatus.OK);
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<Object> handleProductException(
            ProductException ex, WebRequest request) {
        logger.info(ex);
        ErrorDTO error= new ErrorDTO("Error",ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleOtherException(
            Exception ex, WebRequest request) {
        logger.info(ex);
        ErrorDTO error= new ErrorDTO("SYSTEM","SYSTEM ERROR");
        return new ResponseEntity<>(error, HttpStatus.OK);
    }

}
