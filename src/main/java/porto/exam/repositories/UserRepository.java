package porto.exam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import porto.exam.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String>  {
    Optional<User> findByEmail(String email);
}
