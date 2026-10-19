
package dental.flow.api.boot.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException exception) {
        String cause = exception.getCause() != null && exception.getCause().getMessage() != null
                ? exception.getCause().getMessage()
                : null;

        ErrorResponse response = new ErrorResponse(
                exception.getCode(),
                exception.getStatus().value(),
                exception.getMessage(),
                cause
        );

        return ResponseEntity.status(exception.getStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception exception) {
        ErrorResponse response = new ErrorResponse(
                "GEN001",
                500,
                "Erro interno inesperado.",
                exception.getMessage()
        );

        return ResponseEntity.status(500).body(response);
    }
}
