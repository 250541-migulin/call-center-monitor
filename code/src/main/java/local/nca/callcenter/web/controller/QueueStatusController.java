package local.nca.callcenter.web;

import local.nca.callcenter.domain.model.QueueStatus;
import local.nca.callcenter.infrastructure.asterisk.AsteriskQueueMonitorAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST контроллер для мониторинга статуса очереди колл-центра.
 * Предоставляет HTTP API для получения информации о состоянии очереди.
 */
@Slf4j
@RestController
@RequestMapping("/api/queue")
public class QueueStatusController {

    private final AsteriskQueueMonitorAdapter queueMonitor;

    public QueueStatusController(AsteriskQueueMonitorAdapter queueMonitor) {
        this.queueMonitor = queueMonitor;
    }

    /**
     * Получить текущий статус очереди
     * @return статус очереди
     */
    @GetMapping("/status")
    public ResponseEntity<QueueStatus> getQueueStatus() {
        try {
            String queueName = "support";
            QueueStatus status = queueMonitor.getCurrentStatus(queueName);

            log.info("Запрос статуса очереди '{}': {}", queueName, status);
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            log.error("Ошибка при получении статуса очереди: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Проверить активность мониторинга
     * @return true если мониторинг активен
     */
    @GetMapping("/monitoring/active")
    public ResponseEntity<Boolean> isMonitoringActive() {
        boolean active = queueMonitor.isMonitoringActive();
        log.debug("Статус мониторинга: {}", active);
        return ResponseEntity.ok(active);
    }

    /**
     * Получить информацию о системе
     * @return информация о системе
     */
    @GetMapping("/info")
    public ResponseEntity<SystemInfo> getSystemInfo() {
        SystemInfo info = new SystemInfo(
                "Call Center Monitor",
                "1.0.0",
                queueMonitor.isMonitoringActive(),
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(info);
    }

    /**
     * DTO для информации о системе
     */
    @lombok.Data
    public static class SystemInfo {
        private final String applicationName;
        private final String version;
        private final boolean monitoringActive;
        private final long timestamp;
    }
}