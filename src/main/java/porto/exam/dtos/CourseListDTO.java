package porto.exam.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import porto.exam.dtos.detail.CourseListExamDTO;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class CourseListDTO {
    private Integer courseTeacherId;
    private String name;
    private String teacherName;
    private List<CourseListExamDTO> exams;

    public CourseListDTO(Integer courseTeacherId, String name, String teacherName) {
        this.courseTeacherId = courseTeacherId;
        this.name = name;
        this.teacherName = teacherName;
        this.exams = new ArrayList<>();
    }

}
