package porto.exam.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class HttpResponseDto<T> {
    private Boolean isError;
    private String message;
    private T data;
}
