package porto.exam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import porto.exam.dtos.ExamDataDTO;
import porto.exam.entities.StudentExam;

import java.util.Optional;

public interface StudentExamRepository extends JpaRepository<StudentExam, Integer> {
    public Optional<StudentExam> findOneByStudentIdAndExamId(Integer studentId, Integer examId);

    @Query(value = """
        SELECT new porto.exam.dtos.ExamDataDTO(
           se.id,
           e.id,
           e.type,
           e.courseTeacher.course.name,
           e.endDate
        )
        FROM Exam e
        LEFT JOIN StudentExam se ON e = se.exam AND se.student.id = :studentId
        WHERE e.id = :examId
        """)
    public Optional<ExamDataDTO> getByExamAndStudent(@Param("examId") Integer examId, @Param("studentId") Integer studentId);
}
