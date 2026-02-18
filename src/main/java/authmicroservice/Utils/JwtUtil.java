package authmicroservice.Utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

public class JwtUtil {
    private static final String SECRETE_KEY = "v3ryLongAndRand0mSecretKeyAtLeast32Bytes!!";


    private static SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRETE_KEY.getBytes());
    }

    public static String generateToken(String username) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(3600);

        return Jwts.builder()
                .subject(username)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(getSigningKey())
                .compact();
    }
}
