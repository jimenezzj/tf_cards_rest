package com.tfcards.tf_cards_rest.tf_cards_rest.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfcards.tf_cards_rest.tf_cards_rest.configuration.security.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.tfcards.tf_cards_rest.tf_cards_rest.configuration.security.UserPasswordAuthFilterJwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Profile("H2_DB")
@RequiredArgsConstructor
public class SecurityConfigurationJwtV1 {

    private final PasswordEncoder globalEnconder;
    private final UserDetailsService userDetailsService;
//    private final UserPasswordAuthFilterJwt userPassAuthJwtFilter;

    private final ObjectMapper objMapper;
    private final JwtUtils jwtUtils;


    @Bean
    public SecurityFilterChain securityFilteeChain(HttpSecurity http, AuthenticationManager authBuilder) throws Exception {
//        var userPassAuthJwtFilter = new UserPasswordAuthFilterJwt(authBuilder, this.objMapper, this.jwtUtils);
        http
                .securityMatcher("/api/v1/**", "/v1/**")
                .authorizeHttpRequests(authRes -> {
                    authRes
                            .anyRequest()
                            .authenticated();
                })
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(conf -> conf.disable());
        return http.build();

    }

//    @Bean
//    public AuthenticationManager authManager(AuthenticationManagerBuilder authBuilder) throws Exception {
//        authBuilder.userDetailsService(userDetailsService).passwordEncoder(globalEnconder);
//        return authBuilder.build();
//    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration authConfiguration) throws Exception {
        return authConfiguration.getAuthenticationManager();
    }
}
