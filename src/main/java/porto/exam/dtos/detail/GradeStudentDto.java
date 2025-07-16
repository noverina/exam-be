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
public class GradeStudentDto {
    private String studentId;
    private String studentName;
    private Integer grade;
    @JsonProperty("isPass")
    private boolean isPass;
}
