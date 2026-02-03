package local.nca.callcenter.web;

import local.nca.callcenter.application.OriginateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Контроллер для инициации исходящих вызовов.
 */
@Slf4j
@RestController
@RequestMapping("/api/calls")
@RequiredArgsConstructor
public class OriginateController {

    private final OriginateService originateService;

    /**
     * Инициировать вызов на оператора.
     * @param request { "extension": "1001", "callerId": "Client #123" }
     */
    @PostMapping("/originate")
    public ResponseEntity<Map<String, Object>> originateCall(
            @RequestBody OriginateRequest request) {

        Map<String, Object> result = originateService.originateCall(
                request.getExtension(),
                request.getCallerId()
        );

        if ("success".equals(result.get("status"))) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
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