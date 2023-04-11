package com.tfcards.tf_cards_rest.tf_cards_rest.commands;

import java.time.LocalDate;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserTf {

    private Long id;

    @Size(min = 3, max = 255, message = "Name should be into the specified boundaries min 3 and max 0f 255")
    private String name;

    @Size(min = 3, max = 255, message = "Lastname should be into the specified boundaries min 3 and max 0f 255")
    private String lastName;

    @Past(message = "Birth Date is invalid because it is ahead of today")
    private LocalDate birthDate;

    public UserTf(
            @Size(min = 3, max = 255, message = "Name should be into the specified boundaries min 3 and max 0f 255") String name,
            @Size(min = 3, max = 255, message = "Lastname should be into the specified boundaries min 3 and max 0f 255") String lastName,
            @Past(message = "Birth Date is invalid because it is ahead of today") LocalDate birthDate) {
        this.name = name;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

}
