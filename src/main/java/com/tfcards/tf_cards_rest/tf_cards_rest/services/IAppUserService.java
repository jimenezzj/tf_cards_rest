package com.tfcards.tf_cards_rest.tf_cards_rest.services;

import com.tfcards.tf_cards_rest.tf_cards_rest.commands.AppUserDto;

public interface IAppUserService {

    AppUserDto getById(Long id);

    AppUserDto getByEmail(String email);

    AppUserDto saveUser(AppUserDto newUser);
}
