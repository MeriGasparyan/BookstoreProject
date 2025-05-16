package org.example.bookstoreproject.security.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class JwtUtil {

    public static final long JWT_TOKEN_VALIDITY = 1000L * 60 * 60; // 1 hour
    public static final long JWT_REFRESH_TOKEN_VALIDITY = 1000L * 60 * 60 * 24 * 30; // 30 days
    public static final String AUTH_TYPE = "Bearer ";
    private static final String ROLES = "roles";
    private static final String TOKEN_TYPE = "token_type";
    private static final String ACCESS_TOKEN = "access";
    private static final String REFRESH_TOKEN = "refresh";

    @Value("${jwt.secret}")
    private String secret;

    public String generateAccessToken(UserDetails userDetails) {
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .withIssuedAt(Instant.now())
                .withClaim(ROLES, userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .withClaim(TOKEN_TYPE, ACCESS_TOKEN)
                .sign(this.getAlgorithm());
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JWT_REFRESH_TOKEN_VALIDITY))
                .withIssuedAt(Instant.now())
                .withClaim(TOKEN_TYPE, REFRESH_TOKEN)
                .sign(this.getAlgorithm());
    }

    public boolean isRefreshToken(String token) {
        try {
            DecodedJWT jwt = verifyAndDecode(token);
            Claim typeClaim = jwt.getClaim(TOKEN_TYPE);
            return typeClaim != null && REFRESH_TOKEN.equals(typeClaim.asString());
        } catch (JWTVerificationException e) {
            return false;
        }
    }


    public String getUsername(String token) {
        return this.verifyAndDecode(token).getSubject();
    }

    public String[] getAuthorities(String token) {
        return this.verifyAndDecode(token).getClaim(ROLES).asArray(String.class);
    }

    public boolean isVerified(String token) {
        try {
            this.verifyAndDecode(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secret.getBytes(UTF_8));
    }

    private DecodedJWT verifyAndDecode(String token) {
        return JWT.require(this.getAlgorithm()).build().verify(token);
    }


    public boolean isTokenExpired(String token) {
        try {
            final Date expiration = verifyAndDecode(token).getExpiresAt();
            return expiration.before(new Date());
        } catch (JWTVerificationException e) {
            return true;
        }
    }

    public Instant getExpirationDateFromToken(String token) {
        try {
            return verifyAndDecode(token).getExpiresAt().toInstant();
        } catch (JWTVerificationException e) {
            throw new IllegalArgumentException("Invalid token", e);
        }
    }
}
