package porto.exam.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import porto.exam.dtos.detail.ExamSubmitSelectedDTO;

import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ExamSubmitDTO {
    private Integer examId;
    private Integer studentId;
    @JsonProperty("isFinal")
    private boolean isFinal;
    private List<ExamSubmitSelectedDTO> formSubmitSelected;
}

