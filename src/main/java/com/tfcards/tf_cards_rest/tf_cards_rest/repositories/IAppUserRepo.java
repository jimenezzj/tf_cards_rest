package com.tfcards.tf_cards_rest.tf_cards_rest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tfcards.tf_cards_rest.tf_cards_rest.domain.AppUser;
import java.util.List;
import java.util.Optional;

public interface IAppUserRepo extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsernameEquals(String username);
}
