package porto.exam.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import porto.exam.dtos.CourseListDTO;
import porto.exam.repositories.EnrollmentRepository;
import porto.exam.repositories.ExamRepository;
import porto.exam.services.CourseService;

import java.time.ZoneId;
import java.util.List;

@Service
public class ICourseService implements CourseService {
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private ExamRepository examRepository;

    @Override
    public List<CourseListDTO> getByStudent(Integer studentId, String timezone) {
        ZoneId zone = ZoneId.of(timezone);
        var courses = enrollmentRepository.getByStudent(studentId);
        for (var course : courses) {
            var exams = examRepository.getByCourseTeacherAndStudent(course.getCourseTeacherId(), studentId);
            for (var exam : exams) {
                exam.setStartDate(exam.getStartDate().withZoneSameInstant(zone));
                exam.setEndDate(exam.getStartDate().withZoneSameInstant(zone));
                if (exam.getSubmitDate() != null) exam.setSubmitDate(exam.getSubmitDate().withZoneSameInstant(zone));
            }
            course.setExams(exams);
        }
        return courses;
    }
}
