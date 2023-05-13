package com.tfcards.tf_cards_rest.tf_cards_rest.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    // private final JwtEncoder jwtEncoder;

    public SecurityConfiguration() {
        // this.jwtEncoder = jwtEncoder;
    }

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

    // @Autowired
    // public void configureGlobalAuthManager(AuthenticationManagerBuilder authManagerBuilder, DataSource dSource,
    //         PasswordEncoder passEncoder)
    //         throws Exception {
    //     // var admin = User
    //     // .withUsername("admin")
    //     // // .password(passEncoder.encode("dummyadmin"))
    //     // .password(passEncoder.encode("dummyadmin"))
    //     // .roles("CLIENT", "ADMIN")
    //     // .build();
    //     // var client = User
    //     // .withUsername("client")
    //     // .roles("CLIENT")
    //     // .password(passEncoder.encode("dummyclient"));
    //     authManagerBuilder.jdbcAuthentication().dataSource(dSource).withDefaultSchema();
    //     // authManagerBuilder.jdbcAuthentication().dataSource(dSource)
    //     // .withDefaultSchema().withUser(admin).withUser(client);
    // }

    /**
     * It provides a custom implt of datasrouce. Datasource prpos in .properties
     * files
     * may stop working
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

    @Bean
    public UserDetailsService userDetailsService(DataSource dSource, PasswordEncoder passEncoder) {
        var admin = User
                .withUsername("admin")
                // .password(passEncoder.encode("dummyadmin"))
                .password(passEncoder.encode("dummyadmin"))
                .roles("CLIENT", "ADMIN")
                .build();
        var client = User
                .withUsername("client")
                .roles("CLIENT")
                .password(passEncoder.encode("dummyclient"))
                .build();
        // It doent store users on any db structure
        // return new InMemoryUserDetailsManager(client, admin);
        var jdbcUserDetailNamager = new JdbcUserDetailsManager(dSource);
        jdbcUserDetailNamager.createUser(admin);
        jdbcUserDetailNamager.createUser(client);
        return jdbcUserDetailNamager;
    }
}
