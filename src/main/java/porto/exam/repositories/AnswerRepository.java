package porto.exam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import porto.exam.entities.Answer;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    public List<Answer> findByQuestionId(Integer questionId);
}