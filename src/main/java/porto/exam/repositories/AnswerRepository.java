package porto.exam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import porto.exam.entities.Answer;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, String> {
    public List<Answer> findByQuestionQuestionId(String questionId);

    @Query("""
        SELECT a.answerId
        FROM Answer a
        JOIN StudentAnswer sa ON sa.answer = a AND sa.student.userId = :studentId
        WHERE a.question.questionId = :questionId
        """)
    public String findIdByStudentAndQuestion(@Param("studentId") String studentId, @Param("questionId") String questionId);

    @Query("""
        SELECT a
        FROM Answer a
        JOIN FETCH a.question q
        WHERE q.exam.examId = :examId
        """)
    public List<Answer> findExamCorrectAnswers(@Param("examId") String examId);
}