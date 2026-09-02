package com.proxy.ecpcatalogservice.controller;

import com.proxy.ecpcatalogservice.dto.ResourceNotFoundErrorResponse;
import com.proxy.ecpcatalogservice.dto.ValidationErrorResponse;
import com.proxy.ecpcatalogservice.exception.ResourceNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandlingAdvice {

    private static final String VALIDATION_FAILED_MESSAGE = "Validation failed";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleValidationException(MethodArgumentNotValidException ex) {

        final var errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())));

        final var validationErrorResponse = new ValidationErrorResponse(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(), VALIDATION_FAILED_MESSAGE, errors);

        return validationErrorResponse;

    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResourceNotFoundErrorResponse handleResourceNotFoundException(ResourceNotFoundException ex) {

        final var resourceNotFoundResponse = new ResourceNotFoundErrorResponse(LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(), ex.getMessage());

        return resourceNotFoundResponse;

    }

}
