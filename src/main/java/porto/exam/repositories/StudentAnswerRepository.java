package porto.exam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import porto.exam.entities.StudentAnswer;

import java.util.List;

public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Integer> {
    public StudentAnswer findOneByQuestionIdAndAnswerId(Integer questionId, Integer answerId);

    @Query(value = """
        SELECT sa
        FROM StudentAnswer sa
        JOIN sa.studentExam se
        JOIN se.exam e
        WHERE e.courseTeacher.id = :courseTeacherId AND e.id = :examId
        """)
    public List<StudentAnswer> getByCourseTeacherAndExam(@Param("courseTeacherId") Integer courseTeacherId, @Param("examId") Integer examId);
}
