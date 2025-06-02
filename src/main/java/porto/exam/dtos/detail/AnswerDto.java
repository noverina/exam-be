package porto.exam.dtos.detail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class AnswerDto {
    private String answerId;
    private String text;
    @JsonProperty("isCorrect")
    private Boolean isCorrect;
}
