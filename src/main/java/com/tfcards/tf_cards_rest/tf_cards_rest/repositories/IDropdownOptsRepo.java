package com.tfcards.tf_cards_rest.tf_cards_rest.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tfcards.tf_cards_rest.tf_cards_rest.domain.DropdownOptions;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.enums.EDropdownCollection;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.enums.EDropdownCollection.Lang;

public interface IDropdownOptsRepo extends JpaRepository<DropdownOptions, Long> {
    Optional<DropdownOptions> findByCollectionNameAndValue(EDropdownCollection collectName, String value);
}
