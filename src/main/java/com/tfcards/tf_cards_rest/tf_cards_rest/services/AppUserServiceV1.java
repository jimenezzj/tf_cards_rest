package com.tfcards.tf_cards_rest.tf_cards_rest.services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tfcards.tf_cards_rest.tf_cards_rest.commands.AppUserDto;
import com.tfcards.tf_cards_rest.tf_cards_rest.mappers.IAppUserMapper;
import com.tfcards.tf_cards_rest.tf_cards_rest.repositories.IAppUserRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppUserServiceV1 implements IAppUserService, UserDetailsService {

    private final IAppUserRepo appUserRepo;
    private final IAppUserMapper appUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var foundUser = this.getByEmail(username);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        // TODO: add logic to implement role to AppUser and map them here
        return new User(foundUser.getEmail(), foundUser.getPassword(), authorities);
    }

    @Override
    public AppUserDto getById(Long id) {
        return this.appUserMapper.convertToAppUserDto(
                this.appUserRepo.findById(id)
                        .orElseThrow(() -> new UsernameNotFoundException("User with given id cannot be found")));
    }

    @Override
    public AppUserDto getByEmail(String email) {
        log.info("User with email {} was tried to be fetched", email);
        return this.appUserMapper.convertToAppUserDto(
                this.appUserRepo.findByUsernameEquals(email)
                        .orElseThrow(() -> new UsernameNotFoundException("User with given id cannot be found")));
    }

    @Override
    public AppUserDto saveUser(AppUserDto newUser) {
        var foundUser = this.appUserRepo.findByUsernameEquals(newUser.getEmail());
        if (foundUser.isPresent())
            throw new RuntimeException(
                    String.format("The email $s is already registered", newUser.getEmail()));
        var storedUser = this.appUserRepo.saveAndFlush(this.appUserMapper.convertToAppUserEntity(newUser));
        return this.appUserMapper.convertToAppUserDto(storedUser);
    }

}
