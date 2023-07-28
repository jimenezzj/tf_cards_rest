package com.tfcards.tf_cards_rest.tf_cards_rest.configuration;

import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.tfcards.tf_cards_rest.tf_cards_rest.configuration.security.JwtFilter;
import com.tfcards.tf_cards_rest.tf_cards_rest.repositories.list.IUserPhraseRepoList;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import static org.springframework.security.config.Customizer.*;

@Profile({ "LIST_DB", "default" })
@Configuration
@RequiredArgsConstructor
public class SecurityConfigurationJwt {

    private final JwtFilter jwtFilter;
    private final PasswordEncoder globalPassEncoder;
    private final IUserPhraseRepoList userRepoList;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        // * In case default csrf want to be disabled
        security
                .securityMatcher("/v1/**")
                .authorizeHttpRequests(authorize -> {
                    authorize
                            .requestMatchers("/v1/**")
                            .authenticated().and()
                            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
                })
                .authenticationProvider(authenticationProvider(globalPassEncoder))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(s -> s.disable())
                .sessionManagement(conf -> conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
                // .httpBasic(withDefaults());
        // It was enable to rame from h2 console
        security.headers(h -> h.frameOptions().sameOrigin());
        return security.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passEncoder) {
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passEncoder);
        authenticationProvider.setUserDetailsService(userDetailsInMemoryService());
        return authenticationProvider;
    }

    @Bean
    public UserDetailsService userDetailsInMemoryService() {
        // ? It doent store users on any db structure
        // ? return new InMemoryUserDetailsManager(client, admin);
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                return userRepoList.findAll()
                        .stream()
                        .filter(crrUser -> crrUser.getUsername().equals(email))
                        .findFirst()
                        .orElseThrow(() -> new UsernameNotFoundException(email));
            }
        };
    }

    /**
     * It provides a custom implt of datasrouce. Datasource prpos in .properties
     * files may stop working
     * 
     * @return
     */
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setName("tf_cards_db")
                .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
                .build();
    }

}
