package porto.exam.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import porto.exam.annotations.UuidV6;
import porto.exam.enums.ExamType;
import porto.exam.utils.ZonedDateTimeConverter;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Exam {
    @Id
    @UuidV6
    private String examId;
    @Enumerated(EnumType.STRING)
    private ExamType type;
    private Integer passingGrade;
    @Convert(converter = ZonedDateTimeConverter.class)
    private ZonedDateTime startDate;
    @Convert(converter = ZonedDateTimeConverter.class)
    private ZonedDateTime endDate;
    private Boolean isGraded;

    @ManyToOne
    @JoinColumn(name = "course_teacher_id")
    private CourseTeacher courseTeacher;
}
