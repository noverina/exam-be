package porto.exam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import porto.exam.entities.StudentExam;

public interface StudentExamRepository extends JpaRepository<StudentExam, Integer> {
    public StudentExam findOneByStudentIdAndExamId(Integer studentId, Integer examId);
}
