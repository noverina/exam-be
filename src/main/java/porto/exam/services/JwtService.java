package porto.exam.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import porto.exam.repositories.UserRepository;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.access.expiration}")
    private long accessExpiration;
    @Value("${jwt.refresh.expiration}")
    private long refreshExpiration;
    @Autowired
    private UserRepository repo;

    private SecretKey key() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String email, String type) {
        var entity = repo.findByEmail(email).orElseThrow();
        var expiration = type.equals("refresh") ? refreshExpiration : accessExpiration;
        var expirationUnix = Instant.now().getEpochSecond() + expiration;
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(Date.from(Instant.ofEpochSecond(expirationUnix)))
                .claim("role", List.of("ROLE_" + entity.getRole()))
                .signWith(key())
                .compact();
    }

    public Authentication getAuthentication(String token) {
        var claims = validateAndGetClaims(token);

        @SuppressWarnings("unchecked")
        List<String> roles = (List<String>) claims.get("role");
        var authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        var principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public Claims validateAndGetClaims(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}