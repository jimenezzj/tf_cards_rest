package com.tfcards.tf_cards_rest.tf_cards_rest.commands;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiError {

    private String msg;
    // private Set<String> errors;
    private LocalDateTime timestamp;

    public ApiError(String pMsg) {
        this(pMsg, LocalDateTime.now());
    }

    public ApiError(String pMsg, LocalDateTime pTimeStamp) {
        setMsg(pMsg);
        setTimestamp(pTimeStamp);
        // setErrors(Set.of());
    }
}
