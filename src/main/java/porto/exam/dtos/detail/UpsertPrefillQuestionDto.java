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
public class UpsertPrefillQuestionDto {
    private String questionId;
    private String questionText;
    private List<UpsertPrefillAnswerDto> answers;
}
