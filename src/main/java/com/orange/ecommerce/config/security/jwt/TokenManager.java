package com.orange.ecommerce.config.security.jwt;

import com.orange.ecommerce.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenManager {

    @Value("${ecommerce.jwt.secret}")
    private String secret;

    @Value("${ecommerce.jwt.expiration}")
    private Long expirationMillis;

    public static final String AUTHORITIES_KEY = "scopes";

    public String generateToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        final Date now = new Date();
        final Date expiration = new Date(now.getTime() + this.expirationMillis);

        final String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setIssuer("ECOMMERCE")
                .setSubject(Long.toString(user.getId()))
                .claim(AUTHORITIES_KEY, authorities)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, this.secret)
                .compact();
    }

    public boolean isValid(String jwt) {
        try {
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(jwt);
            return true;
        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }

    public Long getUserIdFromToken(String jwt) {
        Claims claims = Jwts.parser().setSigningKey(this.secret)
                .parseClaimsJws(jwt).getBody();
        return Long.parseLong(claims.getSubject());
    }
}
