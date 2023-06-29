package com.tfcards.tf_cards_rest.tf_cards_rest.commands;

import java.util.UUID;

import com.tfcards.tf_cards_rest.tf_cards_rest.domain.enums.EDropdownCollection.Lang;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PhraseTranslationDto {
    
    @NotEmpty
    @Size(min = 5, max = 255)
    private String phraseToTranslate;

    @NotNull
    private Lang from;

    @NotNull
    private Lang to;

}
