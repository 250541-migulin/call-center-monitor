// asterisk/infrastructure/AsteriskQueueMonitor.java
package local.nca.callcenter.asterisk.infrastructure;

import local.nca.callcenter.asterisk.application.port.QueueEventPort;
import local.nca.callcenter.asterisk.config.AsteriskProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

/**
 * Мониторинг очереди колл-центра.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AsteriskQueueMonitor {

    private final AsteriskConnection asteriskConnection;
    private final AsteriskEventObserver eventObserver;
    private final AsteriskProperties properties;
    private final QueueEventPort queueEventPort;

    @PostConstruct
    public void startMonitoring() {
        log.info("Запуск мониторинга очереди '{}'", properties.getQueueName());

        if (asteriskConnection.isConnected()) {
            // Регистрируем порт как слушателя событий
            eventObserver.addListener(queueEventPort);
            eventObserver.registerListeners(asteriskConnection.getConnection());

            log.info("Мониторинг очереди '{}' активирован", properties.getQueueName());
        } else {
            log.warn("Невозможно запустить мониторинг: соединение с Asterisk недоступно");
        }
    }
}