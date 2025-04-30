package porto.exam.dtos.detail;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ExamDataAnswerDTO {
    private Integer answerId;
    private String text;
    @JsonProperty("isCorrect")
    @JsonSetter(nulls = Nulls.SKIP)
    private Boolean isCorrect;

    public ExamDataAnswerDTO(Integer answerId, String text) {
        this.answerId = answerId;
        this.text = text;
    }
}
