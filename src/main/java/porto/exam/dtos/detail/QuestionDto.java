package porto.exam.dtos.detail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {
    private String questionId;
    private String correctAnswerId;
    private String selectedAnswerId;
    private String text;
    private List<AnswerDto> answers;
}
