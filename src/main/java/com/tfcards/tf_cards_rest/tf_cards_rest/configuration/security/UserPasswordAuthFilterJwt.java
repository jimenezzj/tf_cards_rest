package com.tfcards.tf_cards_rest.tf_cards_rest.configuration.security;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.tfcards.tf_cards_rest.tf_cards_rest.commands.AppUserDto;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfcards.tf_cards_rest.tf_cards_rest.domain.AppUser;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;

@Component
//@DependsOn({"authManager"})
@Slf4j
public class UserPasswordAuthFilterJwt extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objMapper;
    private final JwtUtils jwtUtils;

    public UserPasswordAuthFilterJwt(AuthenticationManager authManager, ObjectMapper objMapper, JwtUtils jwtUtils) {
        super.setAuthenticationManager(authManager);
//        this.authManager = authManager;
        this.objMapper = objMapper;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        Authentication authToken = null;
        try {
            var inStream = req.getInputStream();
            var inByteArr = StreamUtils.copyToByteArray(inStream);
            AppUserDto parsedUser = this.objMapper.readValue(inByteArr, AppUserDto.class);
            authToken = this.getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(parsedUser.getEmail(), parsedUser.getPassword(), List.of()));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        if (authToken == null)
            throw new AuthenticationServiceException("App error on trying to parse authentication request body");
        return authToken;
//        return super.attemptAuthentication(req, res);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        var user = (User) authResult.getPrincipal();
        long expDateRefreshToken = System.currentTimeMillis() * 60 * 2;
        var accessToken = this.jwtUtils.generateToken(user, Map.of("iss", request.getRequestURL().toString()));
        var refreshToken = this.jwtUtils.generateToken(user,
                Map.of(
                        "iss", request.getRequestURL().toString(),
                        "exp", Long.toString(expDateRefreshToken))); // ? mil / seg / min
        response.setHeader("access_token", accessToken);
        response.setHeader("refresh_token", refreshToken);
//        super.successfulAuthentication(request, response, chain, authResult);
    }

}
