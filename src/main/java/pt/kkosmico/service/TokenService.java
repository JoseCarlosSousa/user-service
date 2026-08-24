package pt.kkosmico.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pt.kkosmico.dto.RegisterResponseDTO;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class TokenService {

	@Value("${JWT_SECRET}")
	private String secretPhrase;
    private final long expirationTimeInMs = 86400000; // 24 hours in milliseconds

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretPhrase.getBytes(StandardCharsets.UTF_8));
    }
    
    /**
     * Generates a fully signed JSON Web Token using JJWT 0.12+ fluent API.
     */
    public String generateToken(RegisterResponseDTO dto) {
        // Creates a cryptographic key from our secret phrase string
        SecretKey key = Keys.hmacShaKeyFor(secretPhrase.getBytes(StandardCharsets.UTF_8));

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTimeInMs);

        // Modern fluent builder syntax introduced in recent JJWT versions
        return Jwts.builder()
                .subject(dto.getEmail())         // The owner of the token (subject)
                .claim("firstName", dto.getFirstName())    // Custom claim payLoad data
                .claim("lastName", dto.getLastName())    // Custom claim payLoad data
                .claim("userId", dto.getId())    // Custom claim payLoad data
                .claim("role", dto.getRole()) // 🌟 CRUCIAL: Saves the role in the token payLoad
                .issuedAt(now)                    // Token creation timestamp
                .expiration(expiryDate)           // Token death timestamp
                .signWith(key)                    // Cryptographic signature block
                .compact();                       // Compiles everything into a clean string
    }
    
    /**
     * Validates the token and returns the subject (email) if valid.
     */
    public String validateToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject(); // Returns the email
        } catch (Exception e) {
            return null; // Token is invalid or expired
        }
    }

    /**
     * Extracts the specific role claim from the validated token.
     */
    public String getRoleFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.get("role", String.class);
        } catch (Exception e) {
            return null;
        }
    }
}

