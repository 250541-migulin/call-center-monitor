// asterisk/infrastructure/AsteriskEventObserver.java
package local.nca.callcenter.asterisk.infrastructure;

import local.nca.callcenter.asterisk.application.port.QueueEventPort;
import local.nca.callcenter.asterisk.config.AsteriskProperties;
import lombok.extern.slf4j.Slf4j;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.event.LeaveEvent;
import org.asteriskjava.manager.event.QueueEntryEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Обработчик событий Asterisk AMI.
 *
 * Слушает события очереди и уведомляет бизнес через порт QueueEventPort.
 * НЕ зависит от доменных моделей — передаёт только примитивы.
 */
@Slf4j
@Component
public class AsteriskEventObserver {

    private final CopyOnWriteArrayList<QueueEventPort> listeners = new CopyOnWriteArrayList<>();
    private final String queueName;

    public AsteriskEventObserver(AsteriskProperties properties) {
        this.queueName = properties.getQueueName();
        log.info("Создан обработчик событий Asterisk для очереди: {}", queueName);
    }

    /**
     * Регистрирует слушателей событий на соединении Asterisk AMI.
     */
    public void registerListeners(ManagerConnection connection) {
        if (connection == null) {
            log.warn("Невозможно зарегистрировать слушателей: соединение равно null");
            return;
        }

        log.info("Регистрация слушателей событий для очереди '{}'", queueName);

        // Слушатель события: новый вызов в очереди
        connection.addEventListener(event -> {
            if (event instanceof QueueEntryEvent queueEvent &&
                    queueName.equals(queueEvent.getQueue())) {

                log.info("📞 Новый вызов в очереди '{}' от {}",
                        queueEvent.getQueue(),
                        queueEvent.getCallerIdNum());

                // Передаём только примитивы, не создаём доменную модель!
                notifyCallEntered(
                        queueEvent.getUniqueId(),
                        queueEvent.getCallerIdNum(),
                        queueEvent.getQueue()
                );
            }
        });

        // Слушатель события: вызов покинул очередь
        connection.addEventListener(event -> {
            if (event instanceof LeaveEvent leaveEvent &&
                    queueName.equals(leaveEvent.getQueue())) {

                log.info("✅ Вызов покинул очередь '{}': {}",
                        leaveEvent.getQueue(),
                        leaveEvent.getUniqueId());

                notifyCallLeft(leaveEvent.getUniqueId());
            }
        });

        log.info("Слушатели событий зарегистрированы для очереди '{}'", queueName);
    }

    /**
     * Добавить слушателя порта.
     */
    public void addListener(QueueEventPort listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
            log.debug("Добавлен слушатель: {}", listener.getClass().getSimpleName());
        }
    }

    /**
     * Удалить слушателя порта.
     */
    public void removeListener(QueueEventPort listener) {
        listeners.remove(listener);
        log.debug("Удалён слушатель: {}", listener.getClass().getSimpleName());
    }

    /**
     * Уведомить слушателей о новом вызове.
     */
    private void notifyCallEntered(String uniqueId, String callerId, String queueName) {
        for (QueueEventPort listener : listeners) {
            try {
                listener.onCallEntered(uniqueId, callerId, queueName);
            } catch (Exception e) {
                log.error("Ошибка при уведомлении слушателя {}: {}",
                        listener.getClass().getSimpleName(), e.getMessage());
            }
        }
    }

    /**
     * Уведомить слушателей о выходе вызова из очереди.
     */
    private void notifyCallLeft(String uniqueId) {
        for (QueueEventPort listener : listeners) {
            try {
                listener.onCallLeft(uniqueId);
            } catch (Exception e) {
                log.error("Ошибка при уведомлении слушателя {}: {}",
                        listener.getClass().getSimpleName(), e.getMessage());
            }
        }
    }
}