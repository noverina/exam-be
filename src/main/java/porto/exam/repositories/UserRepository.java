package porto.exam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import porto.exam.entities.User;

public interface UserRepository extends JpaRepository<User, Integer>  {

}
