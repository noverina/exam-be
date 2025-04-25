package porto.exam.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import porto.exam.dtos.detail.ExamQuestionAnswerDTO;

import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ExamQuestionDTO {
    private Integer questionId;
    private String text;
    private List<ExamQuestionAnswerDTO> answers;

    public ExamQuestionDTO(Integer id, String text) {
        this.questionId = id;
        this.text = text;
    }
}
