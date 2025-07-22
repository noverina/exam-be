package porto.exam.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import porto.exam.dtos.detail.ExamSubmitQnADto;

import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class SubmitDto {
    private String examId;
    private String studentId;
    @JsonProperty("isFinal")
    private boolean isFinal;
    private List<ExamSubmitQnADto> choices;
}

