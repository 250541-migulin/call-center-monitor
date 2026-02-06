package local.nca.callcenter.asterisk.infrastructure;

import local.nca.callcenter.asterisk.application.port.AsteriskEventListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asteriskjava.manager.ManagerConnection;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Рассылает события всем зарегистрированным бизнес-обработчикам.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AsteriskEventDispatcher {
    // Spring автоматически соберёт все бины AsteriskEventListener
    private final List<AsteriskEventListener> listeners;

    public void startDispatching(ManagerConnection managerConnection) {
        managerConnection.addEventListener(managerEvent -> {
                    for (AsteriskEventListener listener : listeners) {
                      listener.handler(managerEvent);
                    }
                }

                );

        log.info("📡 Диспетчер событий Asterisk запущен (обработчиков: {})", listeners.size());
    }
}


