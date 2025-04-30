package porto.exam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import porto.exam.dtos.detail.CourseListExamDTO;
import porto.exam.entities.Exam;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Integer> {
    @Query(value = """
        SELECT new porto.exam.dtos.detail.CourseListExamDTO(
            e.id,
            e.type,
            se.grade,
            e.passingGrade,
            e.startDate,
            e.endDate,
            se.submitDate
        )
        FROM Exam e
        LEFT JOIN StudentExam se ON e = se.exam AND se.student.id = :studentId
        WHERE e.courseTeacher.id = :courseTeacherId
        """)
    public Optional<List<CourseListExamDTO>> getByCourseTeacherAndStudent(@Param("courseTeacherId") Integer courseTeacherId, @Param("studentId") Integer studentId);

}
