package local.nca.callcenter.asterisk.application.facade;

import local.nca.callcenter.asterisk.infrastructure.AsteriskConnection;
import local.nca.callcenter.asterisk.infrastructure.AsteriskEventDispatcher;
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
    private final AsteriskConnection connection;
    private final AsteriskEventDispatcher dispatcher;

    /**
     * Запустить мониторинг очереди.
     * Регистрирует всех слушателей и активирует прослушку событий AMI.
     */
    public void startMonitoring() {
        if (!connection.isConnected()) {
            connection.connect();
        }

        dispatcher.startDispatching(connection.getManagerConnection());
    }
}