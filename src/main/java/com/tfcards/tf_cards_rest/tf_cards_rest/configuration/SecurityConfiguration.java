package com.tfcards.tf_cards_rest.tf_cards_rest.configuration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        security.authorizeHttpRequests(auth -> {
            auth.anyRequest().authenticated();
        });
        // security.sessionManagement(sm -> {
        // sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // });
        // security.formLogin();
        security.httpBasic();
        // security.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));
        // * In case default csrf want to be disabled
        security.csrf().disable();
        // It was enable to rame from h2 console
        security.headers(h -> h.frameOptions().sameOrigin());
        return security.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var client = User
                .withUsername("client")
                .roles("CLIENT")
                .password(passEncoder().encode("dummyclient"))
                .build();
        var admin = User
                .withUsername("admin")
                .roles("CLIENT", "ADMIN")
                .password(passEncoder().encode("dummyadmin"))
                .build();
        // It doent store users on any db structure
        return new InMemoryUserDetailsManager(client, admin);
    }

    // @Bean
    // public PasswordEncoder passEncoder() {
    // return new BCryptPasswordEncoder();
    // }

    @Bean
    public PasswordEncoder passEncoder() {
        // Encoder seems to be a must when using password
        // this one allows storing password with encoding them
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
