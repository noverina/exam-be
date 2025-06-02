package porto.exam.dtos.detail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import porto.exam.enums.ExamType;

import java.time.ZonedDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class CourseListDetailDto {
    private String examId;
    private ExamType type;
    private Integer passingGrade;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private ZonedDateTime submitDate;
    private Integer grade;
    private Boolean isGraded;
}
