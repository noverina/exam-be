package porto.exam.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import porto.exam.dtos.detail.UpsertPrefillQuestionDto;
import porto.exam.enums.ExamType;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpsertPrefillDto {
    private ExamType type;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private int passingGrade;
    private List<UpsertPrefillQuestionDto> questions;
}
