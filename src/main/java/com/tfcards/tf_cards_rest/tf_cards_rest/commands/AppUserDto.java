package com.tfcards.tf_cards_rest.tf_cards_rest.commands;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AppUserDto {

    private Long id;
    @NotBlank(message = "Email field cannot be empty")
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String name;
}
