package porto.exam.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import porto.exam.dtos.detail.ExamDataQuestionDTO;
import porto.exam.enums.ExamType;

import java.time.ZonedDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ExamDataDTO {
    private Integer studentExamId;
    private Integer examId;
    private ExamType examType;
    private String courseName;
    private ZonedDateTime endDate;
    private List<ExamDataQuestionDTO> questions;

    public ExamDataDTO(Integer studentExamId, Integer examId, ExamType examType, String courseName, ZonedDateTime endDate) {
        this.studentExamId = studentExamId;
        this.examId = examId;
        this.examType = examType;
        this.courseName = courseName;
        this.endDate = endDate;
    }
}
