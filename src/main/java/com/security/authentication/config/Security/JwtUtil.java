package com.security.authentication.config.Security;

import com.security.authentication.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
    private final long EXPIRATION_TIME = 15 * 60 * 1000; // 15 minutes

    // 1. Generate Token from User Roles (Set<Role>)
    public String generateToken(Long idNumber, String name, Set<Role> roles) {
        List<String> roleNames = roles.stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());

        return Jwts.builder()
                .subject(String.valueOf(idNumber))
                .claim("name", name)
                .claim("roles", roleNames) // Injects collection of roles ["ROLE_USER", "ROLE_ADMIN"]
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    // 2. Overloaded Generator for Spring Security GrantedAuthorities
    public String generateTokenFromAuthorities(Long idNumber, String name, Collection<? extends GrantedAuthority> authorities) {
        List<String> roleNames = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .subject(String.valueOf(idNumber))
                .claim("name", name)
                .claim("roles", roleNames)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    // 3. Extract Subject (idNumber) from Token
    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 4. Extract Roles List from Token Claims
    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        return extractClaim(token, claims -> claims.get("roles", List.class));
    }

    // Generic Claim Extractor
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, String idNumber) {
        final String extractedId = extractSubject(token);
        return (extractedId.equals(idNumber) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}