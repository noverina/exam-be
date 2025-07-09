package porto.exam.services;

import porto.exam.dtos.CourseListDto;

import java.util.List;


public interface CourseService {
    List<CourseListDto> fetchByUser(String userId, String role, String timezone);
}
