package local.nca.callcenter.asterisk.application.service;

import local.nca.callcenter.asterisk.infrastructure.AsteriskConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.response.ManagerResponse;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Сервис для инициации исходящих вызовов через Asterisk AMI.
 *
 * Работает с техническими деталями (OriginateAction, ManagerConnection),
 * но скрыт от бизнеса за фасадом AsteriskFacade.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OriginateService {

    private final AsteriskConnection asteriskConnection;

    /**
     * Проверить подключение к Asterisk.
     *
     * @return true если подключён, иначе false
     */
    public boolean isConnected() {
        return asteriskConnection.isConnected();
    }

    /**
     * Инициировать исходящий вызов на оператора.
     *
     * @param extension внутренний номер оператора (например: "1001")
     * @param callerId отображаемый номер/имя (например: "Client #123")
     * @return результат операции
     */
    public Map<String, Object> originateCall(String extension, String callerId) {
        try {
            if (!isConnected()) {
                log.error("Asterisk не подключён");
                return Map.of("status", "error", "message", "Asterisk не подключён");
            }

            ManagerConnection connection = asteriskConnection.getConnection();

            // Формируем номер в формате SIP/1001
            String channel = "PJSIP/" + extension;

            OriginateAction originateAction = new OriginateAction();
            originateAction.setChannel(channel);
            originateAction.setContext("operators");
            originateAction.setExten(extension);
            originateAction.setPriority(1);
            originateAction.setTimeout(30000); // 30 секунд
            originateAction.setCallerId(callerId);

            log.info("📞 Инициирую вызов на {}: {}", extension, callerId);

            ManagerResponse response = connection.sendAction(originateAction, 5000);

            if ("Success".equals(response.getResponse())) {
                log.info("✅ Вызов успешно инициирован: {}", response.getMessage());
                return Map.of(
                        "status", "success",
                        "message", "Вызов инициирован на " + extension,
                        "response", response.getMessage()
                );
            } else {
                log.error("❌ Ошибка инициации вызова: {}", response.getMessage());
                return Map.of(
                        "status", "error",
                        "message", response.getMessage()
                );
            }

        } catch (Exception e) {
            log.error("Исключение при инициации вызова", e);
            return Map.of(
                    "status", "error",
                    "message", e.getMessage()
            );
        }
    }
}