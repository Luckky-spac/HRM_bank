package com.hrm.temp.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (Exception e) {
            System.err.println("JWT Service: Error extracting username from token: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public boolean isTokenValid(String token, String username) {
        try {
            final String tokenUsername = extractUsername(token);
            if (tokenUsername == null) {
                System.err.println("JWT Service: Token username is null");
                return false;
            }
            boolean isValid = tokenUsername.equals(username) && !isTokenExpired(token);
            System.out.println("JWT Service: Token validation - username match: " + tokenUsername.equals(username) + ", expired: " + isTokenExpired(token));
            return isValid;
        } catch (Exception e) {
            System.err.println("JWT Service: Error validating token: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            System.err.println("JWT Service: Error parsing token: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private SecretKey getSigningKey() {
        try {
            // Try to decode as BASE64 first
            byte[] keyBytes = Decoders.BASE64.decode(secret);
            // Ensure key is at least 256 bits (32 bytes) for HS256
            if (keyBytes.length < 32) {
                System.out.println("JWT Service: Key is too short, padding to 32 bytes");
                byte[] padded = new byte[32];
                System.arraycopy(keyBytes, 0, padded, 0, Math.min(keyBytes.length, 32));
                keyBytes = padded;
            }
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            // If BASE64 decode fails, use the secret string directly
            System.out.println("JWT Service: Secret is not BASE64, using as string");
            byte[] keyBytes = secret.getBytes();
            // Ensure key is at least 256 bits (32 bytes) for HS256
            if (keyBytes.length < 32) {
                System.out.println("JWT Service: Key is too short, padding to 32 bytes");
                byte[] padded = new byte[32];
                System.arraycopy(keyBytes, 0, padded, 0, Math.min(keyBytes.length, 32));
                keyBytes = padded;
            }
            return Keys.hmacShaKeyFor(keyBytes);
        }
    }
}
