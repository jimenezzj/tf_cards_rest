package com.tfcards.tf_cards_rest.tf_cards_rest.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tfcards.tf_cards_rest.tf_cards_rest.domain.PhraseBase;

public interface IDemoRepo extends JpaRepository<PhraseBase, Long> {
    Optional<PhraseBase> findByPhraseContaining(String pPhraseSubstr);
}
