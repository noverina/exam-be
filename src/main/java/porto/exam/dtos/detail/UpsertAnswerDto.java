package porto.exam.dtos.detail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpsertAnswerDto {
    private String answerId;
    private String text;
    @JsonProperty("isCorrect")
    private boolean isCorrect;
    @JsonProperty("isNew")
    private boolean isNew;
}
