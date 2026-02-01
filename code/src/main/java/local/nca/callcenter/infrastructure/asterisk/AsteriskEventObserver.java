package local.nca.callcenter.infrastructure.asterisk;

import local.nca.callcenter.config.AsteriskProperties;
import local.nca.callcenter.domain.model.Call;
import local.nca.callcenter.domain.service.CallEventListener;
import lombok.extern.slf4j.Slf4j;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.event.LeaveEvent;
import org.asteriskjava.manager.event.QueueEntryEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Обработчик событий Asterisk AMI.
 * Реализует паттерн Observer для получения событий очереди в реальном времени.
 */
@Slf4j
@Component
public class AsteriskEventObserver {

    private final CopyOnWriteArrayList<CallEventListener> listeners = new CopyOnWriteArrayList<>();
    private final String queueName;

    public AsteriskEventObserver(AsteriskProperties properties) {
        this.queueName = properties.getQueueName();
        log.info("Создан обработчик событий Asterisk для очереди: {}", queueName);
    }

    /**
     * Регистрирует слушателей событий на соединении Asterisk AMI.
     * @param connection соединение с сервером Asterisk
     */
    public void registerListeners(ManagerConnection connection) {
        if (connection == null) {
            log.warn("Невозможно зарегистрировать слушателей: соединение равно null");
            return;
        }

        log.info("Регистрация слушателей событий для очереди '{}'", queueName);

        connection.addEventListener(event -> {
            if (event instanceof QueueEntryEvent queueEvent &&
                    queueName.equals(queueEvent.getQueue())) {

                Call call = new Call(
                        queueEvent.getUniqueId(),
                        queueEvent.getCallerIdNum(),
                        queueEvent.getQueue(),
                        LocalDateTime.now()
                );

                log.info("Новый вызов в очереди '{}' (Caller: {})",
                        queueEvent.getQueue(),
                        queueEvent.getCallerIdNum());

                notifyCallEntered(call);
            }
        });

        connection.addEventListener(event -> {
            if (event instanceof LeaveEvent leaveEvent &&
                    queueName.equals(leaveEvent.getQueue())) {

                String callId = leaveEvent.getUniqueId();
                log.info("Вызов покинул очередь '{}': {}", leaveEvent.getQueue(), callId);
                notifyCallLeft(callId);
            }
        });

        log.info("Слушатели событий зарегистрированы для очереди '{}'", queueName);
    }

    /**
     * Добавляет слушателя событий.
     * @param listener слушатель событий вызовов
     */
    public void addListener(CallEventListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
            log.debug("Добавлен слушатель: {}", listener.getClass().getSimpleName());
        }
    }

    /**
     * Удаляет слушателя событий.
     * @param listener слушатель событий вызовов
     */
    public void removeListener(CallEventListener listener) {
        listeners.remove(listener);
        log.debug("Удалён слушатель: {}", listener.getClass().getSimpleName());
    }

    /**
     * Уведомляет всех зарегистрированных слушателей о новом вызове в очереди.
     * @param call объект вызова
     */
    private void notifyCallEntered(Call call) {
        for (CallEventListener listener : listeners) {
            try {
                listener.onCallEntered(call);
            } catch (Exception e) {
                log.error("Ошибка при уведомлении слушателя {}: {}",
                        listener.getClass().getSimpleName(), e.getMessage());
            }
        }
    }

    /**
     * Уведомляет всех зарегистрированных слушателей о выходе вызова из очереди.
     * @param callId идентификатор вызова
     */
    private void notifyCallLeft(String callId) {
        for (CallEventListener listener : listeners) {
            try {
                listener.onCallLeft(callId);
            } catch (Exception e) {
                log.error("Ошибка при уведомлении слушателя {}: {}",
                        listener.getClass().getSimpleName(), e.getMessage());
            }
        }
    }
}