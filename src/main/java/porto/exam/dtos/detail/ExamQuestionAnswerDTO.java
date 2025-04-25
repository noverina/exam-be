package porto.exam.dtos.detail;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ExamQuestionAnswerDTO {
    private Integer answerId;
    private String text;
}
