package star_wars_card_collector.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

/**
 * Utility class for JWT (JSON Web Token) operations.
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    /**
     * Extracts the username from the JWT token.
     *
     * @param token The JWT token from which to extract the username.
     * @return The username extracted from the token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from the JWT token.
     *
     * @param token The JWT token from which to extract the expiration date.
     * @return The expiration date extracted from the token.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from the JWT token using a custom claims resolver function.
     *
     * @param <T>            The type of the claim value.
     * @param token          The JWT token from which to extract the claim.
     * @param claimsResolver The function that resolves the desired claim from the token's claims.
     * @return The claim value extracted from the token.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Parses and retrieves all claims from the JWT token.
     *
     * @param token The JWT token to parse and extract claims from.
     * @return All claims contained within the JWT token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * Checks if the JWT token is expired.
     *
     * @param token The JWT token to check.
     * @return {@code true} if the token is expired, otherwise {@code false}.
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Generates a JWT token for a given UserDetails object.
     *
     * @param userDetails The UserDetails object representing the user for whom to generate the token.
     * @return The generated JWT token.
     */
    public String generateToken(UserDetails userDetails) {
        return createToken(userDetails.getUsername());
    }

    /**
     * Creates a JWT token with a specified subject (username).
     *
     * @param subject The subject (username) for the JWT token.
     * @return The created JWT token.
     */
    private String createToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours expiration
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    /**
     * Validates a JWT token against a UserDetails object.
     *
     * @param token       The JWT token to validate.
     * @param userDetails The UserDetails object representing the user against which to validate the token.
     * @return {@code true} if the token is valid for the user, otherwise {@code false}.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
