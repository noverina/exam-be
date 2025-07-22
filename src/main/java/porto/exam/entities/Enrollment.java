package porto.exam.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Enrollment {
    @EmbeddedId
    private EnrollmentId id = new EnrollmentId();

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @MapsId("courseTeacherId")
    @JoinColumn(name = "course_teacher_id")
    private CourseTeacher courseTeacher;

    @Embeddable
    public static class EnrollmentId implements Serializable {
        private String studentId;
        private String courseTeacherId;

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof EnrollmentId that)) return false;
            return Objects.equals(studentId, that.studentId) && Objects.equals(courseTeacherId, that.courseTeacherId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(studentId, courseTeacherId);
        }
    }
}


