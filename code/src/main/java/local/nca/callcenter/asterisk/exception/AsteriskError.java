package local.nca.callcenter.asterisk.exception;

import local.nca.callcenter.core.exception.AppErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AsteriskError {
    AST_CONN_TIMEOUT("AST_001", AppErrorType.EXTERNAL_SERVICE, "Таймаут подключения к Asterisk"),
    AST_AUTH_FAILED("AST_002", AppErrorType.EXTERNAL_SERVICE, "Ошибка аутентификации в Asterisk"),
    AST_DISCONNECTED("AST_003", AppErrorType.EXTERNAL_SERVICE, "Соединение с Asterisk разорвано"),
    AST_NETWORK_PROBLEMS("AST_004", AppErrorType.EXTERNAL_SERVICE, "Проблемы с сетью при подключении к Asterisk"),
    AST_CLOSE_ERROR("AST_005", AppErrorType.EXTERNAL_SERVICE, "Ошибка при закрытии соединения с Asterisk");

    private final String code;
    private final AppErrorType type;
    private final String message;
}
