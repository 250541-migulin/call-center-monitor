package local.nca.callcenter.web;

import local.nca.callcenter.infrastructure.asterisk.AsteriskConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * (для теста, потом можно удалить)
 * Контроллер для проверки состояния системы и подключения к Asterisk.
 * Используется для диагностики и мониторинга работоспособности.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/health")
public class HealthCheckController {

    private final AsteriskConnection asteriskConnection;
    /**
     * Проверка общего состояния системы.
     * @return статус системы (UP/ DOWN)
     */
    @GetMapping
    public ResponseEntity<HealthStatus> checkHealth() {
        boolean asteriskConnected = asteriskConnection.isConnected();

        HealthStatus status = new HealthStatus(
                "UP",
                asteriskConnected ? "Asterisk AMI подключён" : "Asterisk AMI НЕ подключён",
                asteriskConnected
        );

        log.info("Проверка состояния системы: {}", status.status);

        if (!asteriskConnected) {
            log.warn("Asterisk AMI не подключён! Проверьте настройки подключения.");
            return ResponseEntity.status(503).body(status);
        }

        return ResponseEntity.ok(status);
    }

    /**
     * Подробная информация о подключении к Asterisk.
     * @return детальная информация о соединении
     */
    @GetMapping("/details")
    public ResponseEntity<ConnectionDetails> getConnectionDetails() {
        boolean connected = asteriskConnection.isConnected();

        ConnectionDetails details = new ConnectionDetails(
                connected,
                connected ? "CONNECTED" : "DISCONNECTED",
                "localhost", // В реальном проекте нужно получить из конфигурации
                5038
        );

        log.debug("Детали подключения к Asterisk: {}", details);

        asteriskConnection.getProperties().logConfiguration();

        return ResponseEntity.ok(details);
    }

    /**
     * DTO для общего состояния системы.
     */
    public record HealthStatus(
            String status,
            String message,
            boolean asteriskConnected
    ) {}

    /**
     * DTO для детальной информации о подключении.
     */
    public record ConnectionDetails(
            boolean connected,
            String connectionState,
            String host,
            int port
    ) {}
}