package pt.kkosmico.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pt.kkosmico.dto.ErrorResponseDTO;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> handleRuntimeException(RuntimeException ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST; // HTTP 400

        // Ensure parameters strictly match: LocalDateTime, int, String, String, String
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                LocalDateTime.now(),
                status.value(),             // Correctly passing the primitive int
                status.getReasonPhrase(),   // "Bad Request"
                ex.getMessage(),            // The exception text message
                request.getRequestURI()     // The URL path requested
        );

        return ResponseEntity.status(status).body(errorResponse);
    }
}
