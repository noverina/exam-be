package porto.exam.dtos.flat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import porto.exam.enums.ExamType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlatGradeDto {
    private String studentId;
    private String studentName;
    private String examId;
    private ExamType examType;
    private String courseName;
    private Integer grade;
    private Integer passingGrade;
}
