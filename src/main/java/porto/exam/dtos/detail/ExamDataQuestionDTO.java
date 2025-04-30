package porto.exam.dtos.detail;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ExamDataQuestionDTO {
    private Integer questionId;
    @JsonSetter(nulls = Nulls.SKIP)
    private Integer selectedAnswerId;
    private String text;
    private List<ExamDataAnswerDTO> answers;

    public ExamDataQuestionDTO(Integer id, String text) {
        this.questionId = id;
        this.text = text;
    }
}
