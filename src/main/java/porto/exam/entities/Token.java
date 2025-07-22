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
public class Token {
    @Id
    @UuidV6
    private String tokenId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private long expiresIn;
    private String token;
}
