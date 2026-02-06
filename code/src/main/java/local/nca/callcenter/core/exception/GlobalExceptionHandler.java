package local.nca.callcenter.core.exception;

import local.nca.callcenter.core.api.response.ApiResponse;
import local.nca.callcenter.core.api.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.UUID;

/**
 * Глобальный обработчик исключений для REST API.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleAppException(final AppException e) {
        log.error("Исключение [{}]: {}", e.getErrorCode(), e.getMessage(), e);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(e.getErrorCode())
                .message(e.getMessage())
                .build();

        return ResponseEntity
                .status(e.getErrorType().getHttpStatus())
                .body(ApiResponse.error(errorResponse));
    }


   @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception e) {
        String internalId = UUID.randomUUID().toString();
        log.error("НЕПРЕДВИДЕННАЯ ОШИБКА [{}]: {}", internalId, e.getMessage(), e);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("internalId =  " + internalId)
                .message("Сообщите администратору internalId = " + internalId)
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
}
