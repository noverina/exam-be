package porto.exam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import porto.exam.dtos.flat.FlatExamQnADto;
import porto.exam.dtos.flat.FlatGradeDto;
import porto.exam.dtos.flat.FlatUpsertPrefillDto;
import porto.exam.entities.Exam;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, String> {

    @Query("""
            SELECT new porto.exam.dtos.flat.FlatUpsertPrefillDto(e.examId, e.type, e.startDate, e.endDate, e.passingGrade, q.questionId, q.text, a.answerId, a.text, a.isCorrect)
            FROM Answer a
            JOIN a.question q
            JOIN q.exam e
            WHERE e.examId = :examId
            """)
    public List<FlatUpsertPrefillDto> fetchUpsertPrefill(@Param("examId") String examId);

    @Query(value = """
            SELECT new porto.exam.dtos.flat.FlatExamQnADto(a.answerId, a.text, a.isCorrect, q.questionId, q.text, e.examId, e.endDate, se.submitDate, e.type, e.isGraded, c.name, se.grade)
            FROM Answer a
            JOIN a.question q
            JOIN q.exam e
            JOIN e.courseTeacher ct
            JOIN ct.course c
            LEFT JOIN StudentAnswer sa ON sa.answer = a AND sa.question = q AND sa.student.id = :studentId
            LEFT JOIN StudentExam se ON se.exam = e AND se.student.id = :studentId
            WHERE e.id = :examId
            ORDER BY q.id, a.id
            """)
    public List<FlatExamQnADto> fetchExamWithQnA(@Param("examId") String examId, @Param("studentId") String studentId);

    @Query("""
            SELECT new porto.exam.dtos.flat.FlatGradeDto(s.userId, s.name, ex.examId, ex.type, c.name, se.grade, ex.passingGrade)
            FROM Enrollment e
            JOIN e.student s
            JOIN e.courseTeacher ct
            JOIN ct.course c
            JOIN Exam ex ON ex.courseTeacher = ct
            LEFT JOIN StudentExam se ON se.exam = ex AND se.student = s
            WHERE ex.examId = :examId and ct.courseTeacherId = :courseTeacherId
            """)
    public List<FlatGradeDto> fetchGrade(@Param("examId") String examId, @Param("courseTeacherId") String courseTeacherId);
}
