package porto.exam.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class StudentExam {
    @EmbeddedId
    private StudentExamId id = new StudentExamId();

    private Integer grade;
    private ZonedDateTime submitDate;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @MapsId("examId")
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @Embeddable
    public static class StudentExamId implements Serializable {
        private String studentId;
        private String examId;

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof StudentExamId that)) return false;
            return Objects.equals(studentId, that.studentId) && Objects.equals(examId, that.examId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(studentId, examId);
        }
    }
}
