package porto.exam.dtos.detail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import porto.exam.enums.DeleteType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpsertDeleteDto {
    private String entityId;
    private DeleteType type;
}
