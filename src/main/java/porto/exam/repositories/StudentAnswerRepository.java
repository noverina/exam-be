package porto.exam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import porto.exam.entities.StudentAnswer;

import java.util.Optional;

public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Integer> {
    public Optional<StudentAnswer> findOneByQuestionIdAndStudentExamId(Integer questionId, Integer studentExamId);
}
