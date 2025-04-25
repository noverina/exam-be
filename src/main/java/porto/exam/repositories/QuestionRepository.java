package porto.exam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import porto.exam.entities.Question;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    public List<Question> findByExamId(Integer examId);

}
