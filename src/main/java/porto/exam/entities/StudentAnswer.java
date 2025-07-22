package porto.exam.entities;

import jakarta.persistence.*;
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
public class StudentAnswer {
    @Id
    @UuidV6
    private String studentAnswerId;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "answer_id")
    private Answer answer;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

}
