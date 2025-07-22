package porto.exam.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import porto.exam.dtos.detail.UpsertDeleteDto;
import porto.exam.dtos.detail.UpsertQuestionDto;
import porto.exam.enums.ExamType;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpsertDto {
    private String examId;
    private String courseTeacherId;
    private ExamType type;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private Integer passingGrade;
    @JsonProperty("isNew")
    private boolean isNew;
    private List<UpsertQuestionDto> questions;
    private List <UpsertDeleteDto> formDelete;

    public UpsertDto(String examId, String courseTeacherId, ExamType type, ZonedDateTime startDate, ZonedDateTime endDate, Integer passingGrade, boolean isNew) {
        this.examId = examId;
        this.courseTeacherId = courseTeacherId;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.passingGrade = passingGrade;
        this.isNew = isNew;
    }
}
