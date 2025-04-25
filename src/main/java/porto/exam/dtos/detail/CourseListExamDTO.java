package porto.exam.dtos.detail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import porto.exam.enums.ExamType;

import java.time.ZonedDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class CourseListExamDTO {
    private Integer examId;
    private ExamType type;
    private Integer grade;
    private Integer passingGrade;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private ZonedDateTime submitDate;
}
