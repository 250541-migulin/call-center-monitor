package local.nca.callcenter.asterisk.application.port;

import org.asteriskjava.manager.event.ManagerEvent;

/**
 * Бизнес-обработчик события Asterisk AMI.
 * Каждый реализующий класс отвечает за ОДНУ бизнес-задачу.
 */
public interface AsteriskEventListener {
    void handler(ManagerEvent event);
}
