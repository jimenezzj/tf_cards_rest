package com.tfcards.tf_cards_rest.tf_cards_rest.services;

import java.util.Optional;
import java.util.Set;

import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseBaseCommand;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.PhraseBase;

import jakarta.validation.Valid;

public interface IDemoService {

    PhraseBaseCommand get(Long id);

    PhraseBaseCommand get(String pPhraseSubstr);

    Set<PhraseBaseCommand> getAll();

    Set<PhraseBaseCommand> getAll(Optional<Integer> limit);

    PhraseBaseCommand create(PhraseBaseCommand newPhrase);

    PhraseBaseCommand update(PhraseBaseCommand phraseToUpdate);

    PhraseBaseCommand patchPhrase(Long id, @Valid PhraseBaseCommand patchedPhrase);

}
