package local.nca.callcenter.operator.domain.model;

import lombok.*;

import java.time.LocalDateTime;


/**
 * Входящий вызов в очереди колл-центра.
 * Содержит информацию о вызове, его статусе и времени ожидания.
 */
@Getter
@RequiredArgsConstructor
@Setter
@EqualsAndHashCode(of = "callId") // сравнение ТОЛЬКО по callId (пока не введён слой бд)
public class Call {

    private final String callId;
    private final String callerId;
    private final LocalDateTime entryTime;
    private CallStatus status = CallStatus.WAITING;;

    public long getWaitingTimeSeconds() {
        return java.time.Duration.between(entryTime, LocalDateTime.now()).getSeconds();
    }
}