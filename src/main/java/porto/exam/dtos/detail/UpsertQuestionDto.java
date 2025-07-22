package porto.exam.dtos.detail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpsertQuestionDto {
    private String questionId;
    private String text;
    private List<UpsertAnswerDto> answers;
    @JsonProperty("isNew")
    private boolean isNew;

    public UpsertQuestionDto(String questionId, String text, boolean isNew) {
        this.questionId = questionId;
        this.text = text;
        this.isNew = isNew;
    }
}
