package com.tfcards.tf_cards_rest.tf_cards_rest.controllers;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping({ "/v2/security", "/security" })
public class SecurityResourceController {

    @GetMapping({ "", "/" })
    public CsrfToken getCsrf(HttpServletRequest req) {
        return (CsrfToken) req.getAttribute("_csrf");
    }

}
