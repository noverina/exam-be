package porto.exam.exceptions;

import lombok.Getter;

@Getter
public class BadLogicException extends RuntimeException {
    public BadLogicException(String message) {
        super(message);
    }

}
