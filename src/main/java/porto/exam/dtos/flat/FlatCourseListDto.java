package porto.exam.dtos.flat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import porto.exam.enums.ExamType;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlatCourseListDto {
    private String courseTeacherId;
    private String courseName;
    private String teacherName;
    private String examId;
    private ExamType type;
    private Integer passingGrade;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private ZonedDateTime submitDate;
    private Integer grade;
    private Boolean isGraded;

    public FlatCourseListDto(String courseTeacherId, String courseName, String teacherName, String examId, ExamType type, Integer passingGrade, ZonedDateTime startDate, ZonedDateTime endDate, Boolean isGraded) {
        this.courseTeacherId = courseTeacherId;
        this.courseName = courseName;
        this.teacherName = teacherName;
        this.examId = examId;
        this.type = type;
        this.passingGrade = passingGrade;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isGraded = isGraded;
    }
}
