package com.tfcards.tf_cards_rest.tf_cards_rest.services;

import java.util.Optional;
import java.util.Set;

import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseBaseCommandV2;

public interface IDemoServiceV2 {
    
    PhraseBaseCommandV2 get(Long id);

    PhraseBaseCommandV2 get(String pPhraseSubstr);

    Set<PhraseBaseCommandV2> getAll();

    Set<PhraseBaseCommandV2> getAll(Optional<Integer> limit);

    PhraseBaseCommandV2 create(PhraseBaseCommandV2 newPhrase);

}
