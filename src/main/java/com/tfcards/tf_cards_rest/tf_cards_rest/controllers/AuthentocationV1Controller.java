package com.tfcards.tf_cards_rest.tf_cards_rest.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class AuthentocationV1Controller {

    

    public ResponseEntity<Map<String, Object>> signIn() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("msg", "Authentication failed"));
    }

}
