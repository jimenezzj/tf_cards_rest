package com.tfcards.tf_cards_rest.tf_cards_rest.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tfcards.tf_cards_rest.tf_cards_rest.commands.AuthReqDto;
import com.tfcards.tf_cards_rest.tf_cards_rest.configuration.security.JwtUtils;
import com.tfcards.tf_cards_rest.tf_cards_rest.repositories.list.IUserPhraseRepoList;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/security")
@RequiredArgsConstructor
public class AuthentocationV1Controller {

    private final IUserPhraseRepoList userPhraseRepoList;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping(path = "/signin")
    public ResponseEntity<Map<String, Object>> signIn(@Valid @RequestBody AuthReqDto authReqDao) {
        // TODO: endpoints res object should be created
        var res = new HashMap<String, Object>(Map.of("msg", null));
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authReqDao.getEmail(), authReqDao.getPassword()));
        var authUser = userPhraseRepoList.findByUsername(authReqDao.getEmail());
        res.put("object", Map.of("access_token", jwtUtils.generateToken(authUser)));
        return ResponseEntity.ok(res);
    }

}
