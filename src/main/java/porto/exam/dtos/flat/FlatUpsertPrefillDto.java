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
public class FlatUpsertPrefillDto {
    private String examId;
    private ExamType type;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private int passingGrade;
    private String questionId;
    private String questionText;
    private String answerId;
    private String answerText;
    private boolean isCorrect;
}
