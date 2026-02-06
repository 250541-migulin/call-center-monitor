package local.nca.callcenter.core.exception;

import lombok.Getter;

/**
 * Базовый класс прикладных исключений.
 */
@Getter
public abstract class AppException extends RuntimeException{
    private final String errorCode;
    private final AppErrorType errorType;

    protected AppException(String errorCode, AppErrorType errorType, String message) {
        super(message);
        this.errorCode = errorCode;
        this.errorType = errorType;
    }

    protected AppException(String errorCode, AppErrorType errorType, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.errorType = errorType;
    }
}
