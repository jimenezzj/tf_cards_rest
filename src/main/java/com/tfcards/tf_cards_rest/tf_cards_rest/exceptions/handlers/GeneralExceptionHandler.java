package com.tfcards.tf_cards_rest.tf_cards_rest.exceptions.handlers;

import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.tfcards.tf_cards_rest.tf_cards_rest.commands.ApiError;

@ControllerAdvice
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    @Nullable
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        var apiErr = new ApiError(ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(apiErr, HttpStatus.NOT_FOUND);
    }

}
