package com.tfcards.tf_cards_rest.tf_cards_rest.exceptions.handlers;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @Override
    @Nullable
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var generalErrMsg = String.format("Invalid data trying to perform a %s in the path %s", null);
        var errRes = new ApiError(generalErrMsg);
        ex.getAllErrors().stream().forEach(err -> {
            var allErrs = errRes.getErrors().get("all");
            if (allErrs == null) {
                errRes.getErrors().put("all", new HashSet<>());
                allErrs = errRes.getErrors().get("all");
            }
            allErrs.add(err.getDefaultMessage());
            if (err instanceof FieldError) {
                var fldErr = (FieldError) err;
                var fldSet = errRes.getErrors().get(fldErr.getField());
                if (fldSet == null) {
                    errRes.getErrors().put(fldErr.getField(), new HashSet<String>());
                    fldSet = errRes.getErrors().get(fldErr.getField());
                }
                fldSet.add(err.getDefaultMessage());
            }
        });
        return ResponseEntity.badRequest().body(errRes);
    }

}
