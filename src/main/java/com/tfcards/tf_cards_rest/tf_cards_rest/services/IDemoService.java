package com.tfcards.tf_cards_rest.tf_cards_rest.services;

import java.util.Optional;
import java.util.Set;

import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseBaseCommand;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.PhraseBase;

public interface IDemoService {

    PhraseBase get(Long id);

    PhraseBase get(String pPhraseSubstr);

    Set<PhraseBase> getAll();

    Set<PhraseBase> getAll(Optional<Integer> limit);

    PhraseBaseCommand create(PhraseBaseCommand newPhrase);

}
