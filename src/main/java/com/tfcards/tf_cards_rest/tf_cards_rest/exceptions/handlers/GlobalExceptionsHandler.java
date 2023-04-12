package com.tfcards.tf_cards_rest.tf_cards_rest.exceptions.handlers;

import java.util.Optional;

import org.hibernate.engine.query.spi.ReturnMetadata;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.tfcards.tf_cards_rest.tf_cards_rest.commands.ApiError;
import com.tfcards.tf_cards_rest.tf_cards_rest.exceptions.ApiVersionNotFound;

@ControllerAdvice
public class GlobalExceptionsHandler {

    @ExceptionHandler(ApiVersionNotFound.class)
    public ResponseEntity<ApiError> handleApiVersionNotAttach(ApiVersionNotFound ex) {
        var defaultErrMsg = "An api version must be specified as Header(x-api-verion=<number>), queryParam(version=<number>) or part of the url(/v<number>/<path>)";
        var apiErr = new ApiError(Optional.of(ex.getMessage()).orElse(defaultErrMsg));
        return new ResponseEntity<>(apiErr, HttpStatus.BAD_REQUEST);
    }

}
