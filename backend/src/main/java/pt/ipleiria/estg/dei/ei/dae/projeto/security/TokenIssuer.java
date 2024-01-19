package pt.ipleiria.estg.dei.ei.dae.projeto.security;

import io.jsonwebtoken.Jwts;
import jakarta.enterprise.context.ApplicationScoped;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
@ApplicationScoped
public class TokenIssuer {
    protected static final byte[] SECRET_KEY =
            "veRysup3rstr0nginv1ncible5ecretkeY@projeto.dae.ipleiria".getBytes();
    protected static final String ALGORITHM = "HMACSHA384";
    public String issue(String username) {
        Key key = new SecretKeySpec(SECRET_KEY, ALGORITHM);
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .signWith(key)
                .compact();
    }
}