package com.tfcards.tf_cards_rest.tf_cards_rest.repositories.list;

import java.util.Set;

import org.springframework.security.core.userdetails.UserDetails;

public interface IUserPhraseRepoList {
    Set<UserDetails> findAll();

    UserDetails findById(Long id);

    UserDetails findByUsername(String email);
}
