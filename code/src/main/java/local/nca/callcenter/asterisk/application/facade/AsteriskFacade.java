package local.nca.callcenter.asterisk.application.facade;

import local.nca.callcenter.asterisk.application.port.QueueEventPort;
import local.nca.callcenter.asterisk.config.AsteriskProperties;
import local.nca.callcenter.asterisk.infrastructure.AsteriskConnection;
import local.nca.callcenter.asterisk.infrastructure.AsteriskEventObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Управление Asterisk AMI.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AsteriskFacade {
    private AsteriskConnection connection;
    private AsteriskEventObserver eventObserver;
    private AsteriskProperties properties;

    /**
     * Запустить мониторинг очереди.
     * Регистрирует всех слушателей и активирует прослушку событий AMI.
     */
    public void startMonitoring() {
        if (!connection.isConnected()) {
            connection.connect();
        }

        eventObserver.registerListeners(connection.getManagerConnection());
    }

    /**
     * Добавить слушателя событий очереди.
     * Слушатель получает ТОЛЬКО примитивы через порт QueueEventPort.
     */
    public void addQueueEventListener(QueueEventPort listener) {
        eventObserver.addListener(listener);
        log.debug("Добавлен слушатель событий: {}", listener.getClass().getSimpleName());
    }

    /**
     * Проверить подключение к Asterisk.
     */
    public boolean isConnected() {
        return connection.isConnected();
    }
}