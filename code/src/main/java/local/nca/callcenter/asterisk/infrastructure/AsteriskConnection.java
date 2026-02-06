package local.nca.callcenter.asterisk.infrastructure;

import local.nca.callcenter.asterisk.exception.AsteriskError;
import local.nca.callcenter.asterisk.exception.AsteriskException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asteriskjava.manager.*;
import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;

import java.io.IOException;

/**
 * Компонент управления соединением с сервером Asterisk AMI.
 */
@Slf4j
@Component
@Getter
@RequiredArgsConstructor
public class AsteriskConnection {

    private final ManagerConnectionFactory managerConnectionFactory;

    private ManagerConnection managerConnection;

    /**
     * Подключение к asterisk AMI
     */
    public void connect() {
        if (managerConnection != null) {
            disconnect();
        }

        try {
            managerConnection = managerConnectionFactory.createManagerConnection();
            managerConnection.login();

        } catch (IOException e) {
            throw new AsteriskException(AsteriskError.AST_NETWORK_PROBLEMS, e);
        } catch (AuthenticationFailedException e) {
            throw new AsteriskException(AsteriskError.AST_AUTH_FAILED, e);
        } catch (TimeoutException e) {
            throw new AsteriskException(AsteriskError.AST_CONN_TIMEOUT, e);
        }
    }

    /**
     * Подключены к asterisk
     * @return
     */
    public boolean isConnected() {
        return managerConnection != null &&
                managerConnection.getState() == ManagerConnectionState.CONNECTED;
    }

    /**
     * Подключение есть, но оно не стабильно
     * @return
     */
    public boolean isNotStableConnection() {
        return managerConnection != null && managerConnection.getState() != ManagerConnectionState.CONNECTED;
    }

    @PreDestroy
    public void disconnect() {
        if (managerConnection == null) {
            log.debug("Нет активного соединения для закрытия");
            return;
        }

        try {
            managerConnection.logoff();
            log.info("Соединение с Asterisk AMI закрыто");
        } catch (Exception e) {
            log.warn("Ошибка при закрытии соединения (игнорируем)", e);
        } finally {
            managerConnection = null;
        }
    }
}