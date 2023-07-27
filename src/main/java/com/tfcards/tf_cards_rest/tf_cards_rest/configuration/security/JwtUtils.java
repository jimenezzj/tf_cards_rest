package com.tfcards.tf_cards_rest.tf_cards_rest.configuration.security;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtils {

    private final String jwtSigninKey = "It_is_super_secret_and_encrypted_I_swear";

    public String generateToken(UserDetails detailsService) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(detailsService, claims);
    }

    /*
     *
     * It's an method abstraction to create the JWT Token
     *
     * @param claims it's the extra information I want to set to the token
     * 
     * @param detailsService service is used to fetche user information
     * 
     */
    public String generateToken(UserDetails detailsService, Map<String, Object> claims) {
        return createToken(detailsService, claims);
    }

    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean hasClaims(String token, String claimName) {
        final Claims claims = extractAllClaims(token);
        return claims.get(claimName) != null;
    }

    /**
     * It creates the JWT token backed on jjwt library
     * 
     * @param userDetails
     * @param claims
     * @return
     */
    private String createToken(UserDetails userDetails, Map<String, Object> claims) {
        return Jwts.builder().setClaims(claims)
                .setSubject(userDetails.getUsername())
                .claim("authorities", userDetails.getAuthorities())
                // ? this line and the next get a Date obj from different ways, just cause I
                // want to try
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(Date.from(LocalDateTime.now().plusHours(24).toInstant(ZoneOffset.UTC)))
                .signWith(SignatureAlgorithm.HS256, jwtSigninKey).compact();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * From JWT body it extracts as key/value pairs the info containded into it
     *  
     * @param token JWT as a string
     * @return
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(jwtSigninKey).parseClaimsJws(token).getBody();
    }

}
