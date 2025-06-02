package porto.exam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import porto.exam.entities.Question;

public interface QuestionRepository extends JpaRepository<Question, String> {
}
