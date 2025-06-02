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
public class FlatExamQnADto {
    private String answerId;
    private String answerText;
    private Boolean isCorrect;
    private String questionId;
    private String questionText;
    private String examId;
    private ZonedDateTime endDate;
    private ZonedDateTime submitDate;
    private ExamType examType;
    private String courseName;
    private Integer grade;
}
