package local.nca.callcenter.operator.application;

import local.nca.callcenter.asterisk.application.port.QueueEventPort;
import local.nca.callcenter.operator.domain.model.Call;
import local.nca.callcenter.operator.domain.model.CallStatus;
import local.nca.callcenter.operator.domain.model.Operator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Сервис управления вызовами оператора.
 *
 * Реализует порт QueueEventPort для получения событий от Asterisk.
 * Владеет доменной моделью Call — создаёт и управляет вызовами.
 */
@Slf4j
@Service
public class CallService implements QueueEventPort {

    private final ConcurrentHashMap<String, Call> calls = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Operator> operators = new ConcurrentHashMap<>();

    // Инициализация операторов при старте
    {
        operators.put("operator1", Operator.builder()
                .id("operator1")
                .extension("1001")
                .name("Оператор 1")
                .status("AVAILABLE")
                .lastActivity(LocalDateTime.now())
                .build());

        operators.put("operator2", Operator.builder()
                .id("operator2")
                .extension("1002")
                .name("Оператор 2")
                .status("AVAILABLE")
                .lastActivity(LocalDateTime.now())
                .build());

        log.info("Инициализированы {} операторов", operators.size());
    }

    // ==================== Реализация порта QueueEventPort ====================

    @Override
    public void onCallEntered(String uniqueId, String callerId, String queueName) {
        // Создаём доменную модель Call из примитивов
        Call call = new Call(uniqueId, callerId, LocalDateTime.now());
        calls.put(call.getCallId(), call);

        log.info("✅ Новый вызов: {} от {} в очереди {}",
                uniqueId, callerId, queueName);
    }

    @Override
    public void onCallLeft(String uniqueId) {
        Call removed = calls.remove(uniqueId);
        if (removed != null) {
            log.info("✅ Вызов {} покинул очередь (ожидал {} сек)",
                    uniqueId, removed.getWaitingTimeSeconds());
        }
    }

    // ==================== Бизнес-методы ====================

    /**
     * Обновить статус вызова.
     */
    public void updateCallStatus(String callId, CallStatus newStatus) {
        Call call = calls.get(callId);
        if (call != null) {
            CallStatus oldStatus = call.getStatus();
            call.setStatus(newStatus);
            log.info("🔄 Статус вызова {} изменён: {} → {}",
                    callId, oldStatus, newStatus);
        }
    }

    /**
     * Обновить статус оператора.
     */
    public void updateOperatorStatus(String operatorId, String newStatus) {
        Operator operator = operators.get(operatorId);
        if (operator != null) {
            operator.setStatus(newStatus);
            operator.setLastActivity(LocalDateTime.now());
            log.info("🔄 Статус оператора {} изменён: {}", operatorId, newStatus);
        }
    }

    // ==================== Методы для контроллера ====================

    public List<Call> getAllCalls() {
        return new ArrayList<>(calls.values());
    }

    public Call getCallById(String callId) {
        return calls.get(callId);
    }

    public void addTestCall(Call call) {
        calls.put(call.getCallId(), call);
        log.info("🧪 Тестовый вызов добавлен вручную: {}", call.getCallId());
    }

    public void removeCall(String callId) {
        calls.remove(callId);
        log.info("🧹 Вызов удалён вручную: {}", callId);
    }

    public List<Operator> getAllOperators() {
        return new ArrayList<>(operators.values());
    }

    public Operator getOperatorById(String operatorId) {
        return operators.get(operatorId);
    }
}