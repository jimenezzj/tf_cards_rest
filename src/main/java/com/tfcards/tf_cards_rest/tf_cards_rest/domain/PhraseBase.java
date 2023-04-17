package com.tfcards.tf_cards_rest.tf_cards_rest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.enums.EPhraseType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PhraseBase {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @JsonIgnore
    private Long id;

    @Size(min = 5, max = 255)
    @NotNull
    @NotEmpty
    private String phrase;

    private EPhraseType phraseType;

    public PhraseBase(Long id, String phrase) {
        setPhraseType(EPhraseType.EXPRESSION);
        setId(id);
        setPhrase(phrase);
    }
}
