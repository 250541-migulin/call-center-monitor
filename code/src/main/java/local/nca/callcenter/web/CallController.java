package local.nca.callcenter.web;

import local.nca.callcenter.application.CallService; // ← Импорт сервиса
import local.nca.callcenter.domain.model.Call;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления и просмотра вызовов в очереди.
 * Чистый веб-слой: не знает про Asterisk, работает только с сервисом.
 */
@Slf4j
@RestController
@RequestMapping("/api/calls")
@RequiredArgsConstructor
public class CallController {

    private final CallService callService;

    @GetMapping
    public ResponseEntity<List<Call>> getAllCalls() {
        List<Call> allCalls = callService.getAllCalls();
        log.info("Запрос всех вызовов: {} шт.", allCalls.size());
        return ResponseEntity.ok(allCalls);
    }

    @GetMapping("/{callId}")
    public ResponseEntity<Call> getCallById(@PathVariable String callId) {
        Call call = callService.getCallById(callId);
        if (call == null) {
            log.warn("Вызов не найден: {}", callId);
            return ResponseEntity.notFound().build();
        }
        log.info("Запрос вызова: {}", callId);
        return ResponseEntity.ok(call);
    }

    @PostMapping
    public ResponseEntity<Call> createTestCall(@RequestBody Call call) {
        callService.addTestCall(call);
        log.info("Создан тестовый вызов: {}", call.getCallId());
        return ResponseEntity.ok(call);
    }

    @DeleteMapping("/{callId}")
    public ResponseEntity<Void> deleteCall(@PathVariable String callId) {
        callService.removeCall(callId);
        log.info("Удалён вызов: {}", callId);
        return ResponseEntity.noContent().build();
    }
}