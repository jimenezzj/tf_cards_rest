package com.tfcards.tf_cards_rest.tf_cards_rest.repositories.list;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class UserPhraseRepoListImpl implements IUserPhraseRepoList {

    private final static Set<UserDetails> APP_DUMMY_USERS = new HashSet<>();

    private final PasswordEncoder passEncoder;

    public UserPhraseRepoListImpl(PasswordEncoder passwordEncoder) {
        this.passEncoder = passwordEncoder;
        var admin = User
                .withUsername("admin")
                .password(passwordEncoder.encode("dummyadmin"))
                .roles("CLIENT", "ADMIN")
                .build();
        var client = User
                .withUsername("client")
                .roles("CLIENT")
                .password(passwordEncoder.encode("dummyclient"))
                .build();
        APP_DUMMY_USERS.add(admin);
        APP_DUMMY_USERS.add(client);
    }

    @Override
    public Set<UserDetails> findAll() {
        return APP_DUMMY_USERS;
    }

    @Override
    public UserDetails findById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public UserDetails findByUsername(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByUsername'");
    }

}
