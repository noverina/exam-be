package porto.exam.exceptions;

import lombok.Getter;

@Getter
public class BadLogicException extends RuntimeException {
    private final int errorCode;

    public BadLogicException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}
