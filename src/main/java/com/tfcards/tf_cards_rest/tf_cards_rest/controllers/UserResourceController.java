package com.tfcards.tf_cards_rest.tf_cards_rest.controllers;

import java.net.URI;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tfcards.tf_cards_rest.tf_cards_rest.commands.UserTf;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = { "/v1/user" })
public class UserResourceController {

    // private final IUserService userService;

    public UserResourceController() {
        // this.userService = userService;
    }

    @GetMapping
    public Set<UserTf> getAll() {
        // return this.userService.getAll();
        return Set.of(
            new UserTf("Selena", "Vargas", LocalDate.now().minusYears(25))
        );
    }

    @GetMapping(path = "/{id}")
    public UserTf getUser(@PathVariable String id) {
        // var foundUser = this.userService.getUser(Long.valueOf(id));
        // return foundUser;
        return new UserTf("Selena", "Vargas", LocalDate.now().minusYears(25));
    }

    @PostMapping(path = { "/", "" })
    public ResponseEntity<UserTf> createUser(@Valid @RequestBody UserTf newUser) {
        var storedUser = new UserTf("Selena", "Vargas", LocalDate.now().minusYears(25));
        // var storedUser = this.userService.save(newUser);
        URI crrLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(storedUser.getId())
                .toUri();
        return ResponseEntity.created(crrLocation).build();
        // return null;
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> delete(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        if (true)
            // if (this.userService.removeById(id))
            response.putAll(Map.of("msg", String.format("User with id(%s) was deleted!", id)));
        else
            response.put("msg", String.format("There/'s no user with hte given id(%s)", id));
        return response;
    }
}