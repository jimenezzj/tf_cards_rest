package com.tfcards.tf_cards_rest.tf_cards_rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ApiVersionNotFound extends RuntimeException {

    public ApiVersionNotFound() {
        super("An api version must be specified as Header(x-api-verion=<number>), queryParam(version=<number>) or part of the url(/v<number>/<path>)");
    }

    public ApiVersionNotFound(String mess) {
        super(mess);
    }

}
