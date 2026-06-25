package civil.common.exception;

import civil.common.Dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex) {
        ErrorResponse response = new ErrorResponse();
        ErrorResponse.ErrorDetail detail = new ErrorResponse.ErrorDetail();

        detail.setCode("INTERNAL_ERROR");
        detail.setMessage(ex.getMessage());
        detail.setStatusCode(500);

        response.setError(detail);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AuthorizationDeniedException ex) {
        ErrorResponse response = new ErrorResponse();
        ErrorResponse.ErrorDetail detail = new ErrorResponse.ErrorDetail();

        detail.setCode("ACCESS_DENIED");
        detail.setMessage("You don't have permission to access this resource");
        detail.setStatusCode(403);

        response.setError(detail);

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
}