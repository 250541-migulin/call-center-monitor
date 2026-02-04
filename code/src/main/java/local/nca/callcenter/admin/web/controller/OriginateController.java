// admin/web/controller/OriginateController.java
package local.nca.callcenter.admin.web.controller;

import local.nca.callcenter.admin.application.service.TestCallService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Контроллер для административных операций с вызовами.
 *
 * Веб-слой: не знает про Asterisk, работает только с TestCallService.
 * Принимает запросы от админа и делегирует их в прикладной сервис.
 */
@Slf4j
@RestController
@RequestMapping("/api/calls")
@RequiredArgsConstructor
public class OriginateController {

    private final TestCallService testCallService;

    /**
     * Инициировать тестовый вызов на оператора.
     *
     * @param request { "extension": "1001", "callerId": "Client #123" }
     * @return результат операции
     */
    @PostMapping("/originate")
    public ResponseEntity<Map<String, Object>> originateCall(
            @RequestBody OriginateRequest request) {

        log.info("📡 Получен запрос на инициацию вызова: {}", request);

        Map<String, Object> result = testCallService.makeTestCall(request.getExtension());

        if ("success".equals(result.get("status"))) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * Проверить состояние подключения к Asterisk.
     *
     * @return статус подключения
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> checkHealth() {
        boolean connected = testCallService.checkAsteriskConnection();

        return ResponseEntity.ok(Map.of(
                "connected", connected,
                "status", connected ? "UP" : "DOWN"
        ));
    }

    /**
     * DTO для запроса инициации вызова.
     */
    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class OriginateRequest {
        private String extension = "1001";  // По умолчанию звоним на 1001
        private String callerId = "Client"; // Отображаемый номер
    }
}