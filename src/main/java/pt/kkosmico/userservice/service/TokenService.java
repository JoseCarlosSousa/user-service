package pt.kkosmico.userservice.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import pt.kkosmico.userservice.model.User;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class TokenService {

    // A secure 256-bit key secret phrase (In production, load this from environment variables)
    private final String secretPhrase = "myUltraSecretKeyForJwtTokenGeneration2026!!!";
    private final long expirationTimeInMs = 86400000; // 24 hours in milliseconds

    /**
     * Generates a fully signed JSON Web Token using JJWT 0.12+ fluent API.
     */
    public String generateToken(User user) {
        // Creates a cryptographic key from our secret phrase string
        SecretKey key = Keys.hmacShaKeyFor(secretPhrase.getBytes(StandardCharsets.UTF_8));

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTimeInMs);

        // Modern fluent builder syntax introduced in recent JJWT versions
        return Jwts.builder()
                .subject(user.getEmail())         // The owner of the token (subject)
                .claim("name", user.getName())    // Custom claim payload data
                .claim("userId", user.getId())    // Custom claim payload data
                .issuedAt(now)                    // Token creation timestamp
                .expiration(expiryDate)           // Token death timestamp
                .signWith(key)                    // Cryptographic signature block
                .compact();                       // Compiles everything into a clean string
    }
}
