package com.tfcards.tf_cards_rest.tf_cards_rest.commands;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiError {

    private String msg;
    private Map<String, Set<String>> errors;
    private LocalDateTime timestamp;

    public ApiError(String pMsg) {
        this(pMsg, LocalDateTime.now());
    }

    public ApiError(String pMsg, LocalDateTime pTimeStamp) {
        setMsg(pMsg);
        setTimestamp(pTimeStamp);
        setErrors(new HashMap<>());
    }
}
