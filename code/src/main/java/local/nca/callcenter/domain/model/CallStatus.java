package local.nca.callcenter.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Статус вызова в очереди колл-центра.
 * Часть доменной модели предметной области.
 */
@Getter
@RequiredArgsConstructor
public enum CallStatus {
    WAITING("В ожидании"),
    RINGING("Звонит оператору"),
    IN_PROGRESS("В работе"),
    COMPLETED("Завершён"),
    ABANDONED("Покинул очередь"),
    MISSED("Пропущен оператором");

    private final String displayName;
}