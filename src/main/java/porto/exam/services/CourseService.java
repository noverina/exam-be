package porto.exam.services;

import porto.exam.dtos.CourseListDTO;

import java.util.List;


public interface CourseService {
    List<CourseListDTO> getByStudent (Integer studentId, String timezone);
}
