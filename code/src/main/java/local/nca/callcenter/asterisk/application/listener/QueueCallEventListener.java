package local.nca.callcenter.asterisk.application.listener;

import local.nca.callcenter.asterisk.application.port.AsteriskEventListener;
import lombok.extern.slf4j.Slf4j;
import org.asteriskjava.manager.event.*;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class QueueCallEventListener implements AsteriskEventListener {
    private final String monitoredQueue= "support";
    private final AtomicInteger waitingCalls = new AtomicInteger(0);

    @Override
    public void handler(ManagerEvent event) {

        log.info("📡 {}", event.getClass().getSimpleName());

        // Для отладки — покажи ключевые поля нужных событий
        if (event instanceof QueueEntryEvent q) {
            log.info("   → QueueEntry: queue={}, callerId={}", q.getQueue(), q.getCallerIdNum());
        }
        if (event instanceof AgentConnectEvent a) {
            log.info("   → AgentConnect: queue={}, agent={}, uniqueid={}",
                    a.getQueue(), a.getMemberName(), a.getUniqueId());
        }
    }
}
