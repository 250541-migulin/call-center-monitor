package local.nca.callcenter.application;

import local.nca.callcenter.domain.model.Call;
import local.nca.callcenter.domain.service.CallEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    @Override
    public void onCallEntered(Call call) {
        calls.put(call.getCallId(), call);
        log.info("Вызов добавлен из Asterisk: {} (Caller: {}, Очередь: {})",
                call.getCallId(), call.getCallerId(), call.getQueueName());
    }

    @Override
    public void onCallLeft(String callId) {
        Call removed = calls.remove(callId);
        if (removed != null) {
            log.info("Вызов удалён из Asterisk: {} (пробывал в очереди {} сек)",
                    callId, removed.getWaitingTimeSeconds());
        }
    }

    // Методы для контроллера
    public List<Call> getAllCalls() {
        return new ArrayList<>(calls.values());
    }

    public Call getCallById(String callId) {
        return calls.get(callId);
    }

    public void addTestCall(Call call) {
        calls.put(call.getCallId(), call);
        log.info("Тестовый вызов добавлен вручную: {}", call.getCallId());
    }

    public void removeCall(String callId) {
        calls.remove(callId);
        log.info("Вызов удалён вручную: {}", callId);
    }
}