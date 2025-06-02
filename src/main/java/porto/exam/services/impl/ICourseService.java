package porto.exam.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import porto.exam.dtos.CourseListDto;
import porto.exam.dtos.detail.CourseListDetailDto;
import porto.exam.dtos.flat.FlatCourseListDto;
import porto.exam.repositories.CourseRepository;
import porto.exam.services.CourseService;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ICourseService implements CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Override
    public List<CourseListDto> fetchByStudent(String studentId, String timezone)  {
        var zone = ZoneId.of(timezone);
        var output = new ArrayList<CourseListDto>();
        var groupedExams = courseRepository.fetchCoursesWithExams(studentId)
                .stream().collect(Collectors.groupingBy(FlatCourseListDto::getCourseTeacherId));
        for (var groupedExam : groupedExams.entrySet()) {
            var courseTeacherId = groupedExam.getKey();
            var courseName = groupedExam.getValue().getFirst().getCourseName();
            var teacherName = groupedExam.getValue().getFirst().getTeacherName();

            var exams = groupedExam.getValue().stream()
                    .filter(exam -> exam.getExamId() != null)
                    .map(exam ->
                            new CourseListDetailDto(exam.getExamId(), exam.getType(), exam.getPassingGrade(), exam.getStartDate().withZoneSameInstant(zone), exam.getEndDate().withZoneSameInstant(zone), exam.getSubmitDate() != null ? exam.getSubmitDate().withZoneSameInstant(zone) : null, exam.getGrade(), exam.getIsGraded()))
                    .toList();
            output.add(new CourseListDto(courseTeacherId, courseName, teacherName, exams));
        }
        return output;
    }
}
