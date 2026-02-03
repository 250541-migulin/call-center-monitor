package local.nca.callcenter.infrastructure.asterisk;

import local.nca.callcenter.application.CallService;
import local.nca.callcenter.config.AsteriskProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

/**
 * Компонент мониторинга очереди колл-центра.
 * Регистрирует слушателей событий Asterisk при старте приложения.
 */
@Slf4j
@Component
@Getter
@RequiredArgsConstructor
public class AsteriskQueueMonitor {

    private final AsteriskConnection asteriskConnection;
    private final AsteriskEventObserver eventObserver;
    private final AsteriskProperties properties;
    private final CallService callService;

    private boolean monitoringActive = false;

    @PostConstruct
    public void startMonitoring() {
        if (monitoringActive) {
            log.warn("Мониторинг уже запущен");
            return;
        }

        log.info("Запуск мониторинга очереди '{}'", properties.getQueueName());

        if (asteriskConnection.isConnected()) {
            // Регистрируем сервис как слушателя событий!
            eventObserver.addListener(callService);
            eventObserver.registerListeners(asteriskConnection.getConnection());
            monitoringActive = true;
            log.info("Мониторинг очереди '{}' активирован. CallService зарегистрирован как слушатель.",
                    properties.getQueueName());
        } else {
            log.warn("Невозможно запустить мониторинг: соединение с Asterisk недоступно");
        }
    }

    @PreDestroy
    public void stopMonitoring() {
        if (monitoringActive) {
            log.info("Остановка мониторинга очереди '{}'", properties.getQueueName());
            monitoringActive = false;
        }
    }
}