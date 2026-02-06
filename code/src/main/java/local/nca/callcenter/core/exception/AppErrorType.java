package local.nca.callcenter.core.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Категории ошибок приложения с привязкой к HTTP-статусам.
 */
@RequiredArgsConstructor
public enum AppErrorType {
    VALIDATION(HttpStatus.BAD_REQUEST),
    AUTHENTICATION(HttpStatus.UNAUTHORIZED),
    AUTHORIZATION(HttpStatus.FORBIDDEN),
    NOT_FOUND(HttpStatus.NOT_FOUND),
    CONFLICT(HttpStatus.CONFLICT),
    EXTERNAL_SERVICE(HttpStatus.BAD_GATEWAY),
    INTERNAL(HttpStatus.INTERNAL_SERVER_ERROR);

    private final HttpStatus status;

    public HttpStatus getHttpStatus() {
        return status;
    }
}