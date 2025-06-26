package porto.exam.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import porto.exam.dtos.detail.GradeStudentDto;
import porto.exam.enums.ExamType;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GradeDto {
    private String examId;
    private ExamType examType;
    private String courseName;
    private Integer passingGrade;
    private Integer passRate;
    private Integer passAmt;
    private Integer average;
    private List<GradeStudentDto> students;
}
