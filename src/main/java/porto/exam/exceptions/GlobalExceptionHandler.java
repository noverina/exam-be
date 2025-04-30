package porto.exam.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import porto.exam.dtos.HttpResponseDTO;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BadLogicException.class)
    public ResponseEntity<HttpResponseDTO<String>> handleBadLogicException(BadLogicException ex) {
        var error = new HttpResponseDTO<String>(true, ex.getMessage(), null);

        if (ex.getCause() != null) log.error("[{} ({})] {}: {}", ex.getStackTrace()[0].getClassName(), ex.getStackTrace()[0].getMethodName(), ex.getCause().toString(), ex.getMessage());
        else log.error("[{} ({})] {}", ex.getStackTrace()[0].getClassName(), ex.getStackTrace()[0].getMethodName(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponseDTO<String>> handleAllException(Exception ex) {
        var error = new HttpResponseDTO<String>(true, ex.getMessage(), null);
        if (ex.getCause() != null) log.error("[{} ({})] {}: {}", ex.getStackTrace()[0].getClassName(), ex.getStackTrace()[0].getMethodName(), ex.getCause().toString(), ex.getMessage());
        else log.error("[{} ({})] {}", ex.getStackTrace()[0].getClassName(), ex.getStackTrace()[0].getMethodName(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }



}
