package com.tfcards.tf_cards_rest.tf_cards_rest.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.tfcards.tf_cards_rest.tf_cards_rest.commands.AppUserDto;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.AppUser;

@Mapper
public interface IAppUserMapper {

    @Mapping(source = "appUser.username", target = "email")
    AppUserDto convertToAppUserDto(AppUser appUser);

    @Mapping(source = "appUserDto.email", target = "username")
    AppUser convertToAppUserEntity(AppUserDto appUserDto);

}
