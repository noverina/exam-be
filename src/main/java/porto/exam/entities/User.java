package porto.exam.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import porto.exam.annotations.UuidV6;
import porto.exam.enums.Role;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Id
    @UuidV6
    private String userId;
    private String name;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
}
