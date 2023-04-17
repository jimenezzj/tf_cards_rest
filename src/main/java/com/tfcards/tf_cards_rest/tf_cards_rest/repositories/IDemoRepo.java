package com.tfcards.tf_cards_rest.tf_cards_rest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tfcards.tf_cards_rest.tf_cards_rest.domain.PhraseBase;

public interface IDemoRepo extends JpaRepository<PhraseBase, Long> {
    PhraseBase findByPhraseContaining(String pPhraseSubstr);
}
