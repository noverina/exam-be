package porto.exam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import porto.exam.entities.Token;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, String> {
    Optional<Token> findByTokenAndExpiresInAfter(String token, long now);

    Optional<Token> findByToken(String token);
}