package com.tfcards.tf_cards_rest.tf_cards_rest.exceptions;

import javax.print.DocFlavor.STRING;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException() {
        super("Entity was not found");
    }

    public EntityNotFoundException(String customErrMsg) {
        super(customErrMsg);
    }

    public EntityNotFoundException(Class entityClass) {
        super(String.format("%s entity was not found", entityClass.getSimpleName()));
    }
}
