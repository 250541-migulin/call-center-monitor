package local.nca.callcenter.infrastructure.asterisk;

import local.nca.callcenter.config.AsteriskProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.ManagerConnectionState;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

/**
 * Компонент управления соединением с сервером Asterisk AMI.
 * Отвечает ТОЛЬКО за жизненный цикл соединения (паттерн: Connection Pool).
 * Конфигурация инжектится через отдельный компонент AsteriskProperties.
 */
@Slf4j
@Component
@Getter
@RequiredArgsConstructor
public class AsteriskConnection {

    private final AsteriskProperties properties;
    private ManagerConnection connection;
    private boolean connected = false;


    @PostConstruct
    public void init() {
        log.info("Инициализация соединения с Asterisk AMI (хост: {}, порт: {})",
                properties.getHost(), properties.getPort());

        try {
            connect();
        } catch (Exception e) {
            log.error("Не удалось установить соединение с Asterisk AMI: {}", e.getMessage());
            if (properties.isAutoReconnect()) {
                log.warn("Авто-переподключение включено. Попытка переподключения через {} мс...",
                        properties.getReconnectInterval());
                // TODO в идеале необходим механизм переподключения
            }
        }
    }

    public void connect() throws Exception {
        log.info("Подключение к Asterisk AMI: {}:{}...", properties.getHost(), properties.getPort());

        try {
            ManagerConnectionFactory factory = new ManagerConnectionFactory(
                    properties.getHost(),
                    properties.getPort(),
                    properties.getUsername(),
                    properties.getPassword()
            );

            connection = factory.createManagerConnection();
            connection.login();

            if (connection.getState() == ManagerConnectionState.CONNECTED) {
                connected = true;
                log.info("Соединение с Asterisk AMI установлено: {}:{}",
                        properties.getHost(), properties.getPort());
            } else {
                throw new IllegalStateException("Подключение не удалось. Статус: " + connection.getState());
            }
        } catch (Exception e) {
            log.error("Ошибка подключения к Asterisk: {}", e.getMessage());
            throw e;
        }
    }

    public boolean isConnected() {
        return connected && connection != null &&
                connection.getState() == ManagerConnectionState.CONNECTED;
    }

    @PreDestroy
    public void disconnect() {
        if (isConnected()) {
            try {
                connection.logoff();
                log.info("Соединение с Asterisk AMI закрыто");
            } catch (Exception e) {
                log.warn("Ошибка при закрытии соединения: {}", e.getMessage());
            }
        }
        connection = null;
        connected = false;
    }
}