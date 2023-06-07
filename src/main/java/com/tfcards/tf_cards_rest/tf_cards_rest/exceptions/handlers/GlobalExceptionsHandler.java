package com.tfcards.tf_cards_rest.tf_cards_rest.exceptions.handlers;

import java.util.HashSet;
import java.util.Optional;
import java.util.function.BiConsumer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.tfcards.tf_cards_rest.tf_cards_rest.commands.ApiError;
import com.tfcards.tf_cards_rest.tf_cards_rest.exceptions.ApiVersionNotFound;
import com.tfcards.tf_cards_rest.tf_cards_rest.exceptions.EntityNotFoundException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionsHandler {

    @ExceptionHandler(ApiVersionNotFound.class)
    public ResponseEntity<ApiError> handleApiVersionNotAttach(ApiVersionNotFound ex) {
        var defaultErrMsg = "An api version must be specified as Header(x-api-verion=<number>), queryParam(version=<number>) or part of the url(/v<number>/<path>)";
        var apiErr = new ApiError(Optional.of(ex.getMessage()).orElse(defaultErrMsg));
        return new ResponseEntity<>(apiErr, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleApiVersionNotAttach(EntityNotFoundException ex) {
        var defaultErrMsg = ex.getMessage();
        var apiErr = new ApiError(defaultErrMsg);
        return new ResponseEntity<>(apiErr, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<ApiError> handleJpaViolation(TransactionSystemException ex) {
        var defaultErrMsg = ex.getMessage();
        var apiErr = new ApiError(defaultErrMsg);
        if (ex.getCause().getCause() instanceof ConstraintViolationException) {
            var consEx = ((ConstraintViolationException) ex.getCause().getCause());
            BiConsumer<String, ConstraintViolation<?>> addFieldErrorMessage = (errName, err) -> {
                var fldName = errName;
                if (apiErr.getErrors().get(fldName) == null)
                    apiErr.getErrors().put(fldName, new HashSet<>());

                apiErr.getErrors().get(fldName).add(err.getMessage());
            };
            consEx.getConstraintViolations().stream().forEach(err -> {
                addFieldErrorMessage.accept("all", err);
                addFieldErrorMessage.accept(err.getPropertyPath().toString(), err);
            });
            return new ResponseEntity<>(apiErr, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(apiErr, HttpStatus.BAD_REQUEST);
    }

}
