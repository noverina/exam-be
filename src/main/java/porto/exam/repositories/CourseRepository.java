package porto.exam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import porto.exam.dtos.flat.FlatCourseListDto;
import porto.exam.entities.Course;

import java.util.Set;

public interface CourseRepository extends JpaRepository<Course, String> {
    @Query("""
            SELECT new porto.exam.dtos.flat.FlatCourseListDto(ct.courseTeacherId, c.name, t.name, e.examId, e.type, e.passingGrade, e.startDate, e.endDate, se.submitDate, se.grade, e.isGraded)
            FROM Exam e
            RIGHT JOIN e.courseTeacher ct ON ct = e.courseTeacher
            JOIN Enrollment er ON er.courseTeacher = ct
            JOIN ct.course c
            JOIN ct.teacher t
            LEFT JOIN StudentExam se ON se.exam = e
            WHERE er.student.userId = :studentId
            """)
    public Set<FlatCourseListDto> fetchCoursesWithExams(@Param("studentId") String studentId);
}
