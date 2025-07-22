package porto.exam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import porto.exam.entities.StudentAnswer;

import java.util.List;
import java.util.Optional;

public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, String> {
    @Query("""
        SELECT sa
        FROM StudentAnswer sa
        WHERE sa.question.questionId = :questionId AND sa.student.userId = :studentId AND sa.question.exam.examId = :examId
        """)
    public Optional<StudentAnswer> findByQuestionAndStudentAndExam(@Param("questionId") String questionId, @Param("studentId") String studentId, @Param("examId") String examId);

    @Query("""
        SELECT sa
        FROM StudentAnswer sa
        JOIN FETCH sa.answer
        WHERE sa.student.userId = :studentId AND sa.question.exam.examId = :examId
        """)
    public Optional<List<StudentAnswer>> findByStudentAndExam(@Param("studentId") String studentId, @Param("examId") String examId);
}
