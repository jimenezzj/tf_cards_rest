package com.tfcards.tf_cards_rest.tf_cards_rest.commands;

import com.tfcards.tf_cards_rest.tf_cards_rest.domain.enums.EPhraseType;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PhraseBaseCommand {

    private Long id;

    @Size(min = 5, max = 255)
    @NotNull
    @NotEmpty(message = "A phrase cannot be empty")
    private String phrase;

    @NotNull(message = "A phrase type muest be specified")
    // @NotEmpty
    private EPhraseType phraseType;

    public PhraseBaseCommand(Long id, String phrase) {
        setPhraseType(EPhraseType.EXPRESSION);
        setId(id);
        setPhrase(phrase);
    }

}
