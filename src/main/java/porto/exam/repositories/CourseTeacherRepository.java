package porto.exam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import porto.exam.entities.CourseTeacher;

public interface CourseTeacherRepository extends JpaRepository<CourseTeacher, Integer> {
}
