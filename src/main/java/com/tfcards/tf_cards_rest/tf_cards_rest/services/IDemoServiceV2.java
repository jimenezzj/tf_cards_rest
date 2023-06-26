package com.tfcards.tf_cards_rest.tf_cards_rest.services;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseDtoV2;
import com.tfcards.tf_cards_rest.tf_cards_rest.commands.PhraseTranslationDto;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.enums.EDropdownCollection;

public interface IDemoServiceV2 {

    Optional<PhraseDtoV2> get(Long id);

    Optional<PhraseDtoV2> get(String pPhraseSubstr);

    Set<PhraseDtoV2> getAll();

    Set<PhraseDtoV2> getAll(Optional<Integer> limit);

    Optional<PhraseDtoV2> create(PhraseDtoV2 newPhrase);

    Optional<PhraseDtoV2> update(Long id, PhraseDtoV2 newPhrase);

    PhraseDtoV2 addTranslationTo(UUID idPhrase, PhraseTranslationDto pTranslationDto);
}
