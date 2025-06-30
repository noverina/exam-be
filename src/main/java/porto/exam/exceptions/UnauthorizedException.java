package porto.exam.exceptions;

import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }

}
