package porto.exam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import porto.exam.dtos.CourseListDTO;
import porto.exam.entities.Enrollment;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    @Query("""
            SELECT new porto.exam.dtos.CourseListDTO(
                ct.id,
                ct.course.name,
                ct.teacher.name
            )
            FROM Enrollment e
            JOIN e.courseTeacher ct
            WHERE e.student.id = :studentId
            """)
    public List<CourseListDTO> getByStudent(@Param("studentId") Integer studentId);
}
