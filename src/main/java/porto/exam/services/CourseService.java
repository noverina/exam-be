package porto.exam.services;

import porto.exam.dtos.CourseListDto;

import java.util.List;


public interface CourseService {
    List<CourseListDto> fetchByStudent(String studentId, String timezone);
}
