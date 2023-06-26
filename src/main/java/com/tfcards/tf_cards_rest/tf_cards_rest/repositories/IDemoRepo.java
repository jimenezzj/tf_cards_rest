package com.tfcards.tf_cards_rest.tf_cards_rest.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.tfcards.tf_cards_rest.tf_cards_rest.domain.PhraseBase;

@RepositoryRestResource(path = "phrase")
public interface IDemoRepo extends JpaRepository<PhraseBase, Long> {
    Optional<PhraseBase> findByPhraseContaining(String pPhraseSubstr);

    List<PhraseBase> findByPhraseIdIs(UUID pPhraseId);
    // Optional<PhraseBase> findByPhraseIdIsAndphraseLangIs();
}
