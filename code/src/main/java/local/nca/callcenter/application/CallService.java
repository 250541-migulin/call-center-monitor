package local.nca.callcenter.application;

import local.nca.callcenter.domain.model.Call;
import local.nca.callcenter.domain.model.CallStatus;
import local.nca.callcenter.domain.model.Operator;
import local.nca.callcenter.domain.service.CallEventListener;
import local.nca.callcenter.domain.service.CallStatusListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Сервис управления вызовами.
 * Реализует паттерн Observer для реакции на события Asterisk.
 * Отвечает за хранение состояния вызовов (пока в памяти).
 */
@Slf4j
@Service
public class CallService implements CallEventListener {

    private final ConcurrentHashMap<String, Call> calls = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Operator> operators = new ConcurrentHashMap<>();
    private final List<CallStatusListener> statusListeners = new ArrayList<>();

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

    @Override
    public void onCallEntered(Call call) {
        calls.put(call.getCallId(), call);
        log.info("✅ Вызов добавлен из Asterisk: {} (Caller: {}, Очередь: {})",
                call.getCallId(), call.getCallerId(), call.getQueueName());
    }

    @Override
    public void onCallLeft(String callId) {
        Call removed = calls.remove(callId);
        if (removed != null) {
            log.info("✅ Вызов удалён из Asterisk: {} (пробывал в очереди {} сек)",
                    callId, removed.getWaitingTimeSeconds());
        }
    }

    /**
     * Обновить статус вызова.
     */
    public void updateCallStatus(String callId, CallStatus newStatus) {
        Call call = calls.get(callId);
        if (call != null) {
            CallStatus oldStatus = call.getStatus();
            call.setStatus(newStatus);

            notifyStatusChange(call, oldStatus, newStatus);
            log.info("🔄 Статус вызова {} изменён: {} → {}", callId, oldStatus, newStatus);
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

    /**
     * Уведомить слушателей о смене статуса.
     */
    private void notifyStatusChange(Call call, CallStatus oldStatus, CallStatus newStatus) {
        for (CallStatusListener listener : statusListeners) {
            try {
                listener.onCallStatusChanged(call, oldStatus, newStatus);
            } catch (Exception e) {
                log.error("Ошибка при уведомлении слушателя: {}", e.getMessage());
            }
        }
    }

    // ================= Методы для контроллера =================

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

    public void addStatusListener(CallStatusListener listener) {
        statusListeners.add(listener);
        log.debug("Добавлен слушатель статусов: {}", listener.getClass().getSimpleName());
    }
}