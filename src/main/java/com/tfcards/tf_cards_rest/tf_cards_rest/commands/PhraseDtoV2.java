package com.tfcards.tf_cards_rest.tf_cards_rest.commands;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.enums.EPhraseType;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonFilter("PhraseFilter")
public class PhraseDtoV2 {

    private Long id;

    @Size(min = 5, max = 255)
    @NotNull
    @NotEmpty(message = "A phrase cannot be empty")
    private String msg;

    @NotNull
    private EPhraseType phraseType;

    @NotNull
    @NotEmpty(message = "Name of author must be specified")
    private String author;

    @FutureOrPresent(message = "publish date must be equal or greater than current date")
    private LocalDate publishDate;

    @JsonIgnore
    private LocalDate createdAt;

}
