package porto.exam.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import porto.exam.annotations.UuidV6;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CourseTeacher {
    @Id
    @UuidV6
    private String courseTeacherId;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacher;
}
