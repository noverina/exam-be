package porto.exam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import porto.exam.entities.StudentExam;

import java.util.List;
import java.util.Optional;

public interface StudentExamRepository extends JpaRepository<StudentExam, StudentExam.StudentExamId> {
    @Query("""
        SELECT se
        FROM StudentExam se
        WHERE se.student.userId = :studentId AND se.exam.examId = :examId
        """)
    public Optional<StudentExam> findByStudentAndExam(@Param("studentId") String studentId, @Param("examId") String examId);

    @Query("""
        SELECT se
        FROM StudentExam se
        JOIN FETCH se.student
        WHERE se.exam.examId = :examId
        """)
    public Optional<List<StudentExam>> findByExam(@Param("examId") String examId);

}
