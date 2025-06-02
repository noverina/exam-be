package porto.exam.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import porto.exam.dtos.detail.QuestionDto;
import porto.exam.enums.ExamType;

import java.time.ZonedDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ExamListDto {
    @JsonProperty("isFinal")
    private boolean isFinal;
    private String examId;
    private ExamType examType;
    private String courseName;
    private ZonedDateTime endDate;
    private Integer grade;
    private List<QuestionDto> questions;
}
