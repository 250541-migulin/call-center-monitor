package local.nca.callcenter.application;

import local.nca.callcenter.infrastructure.asterisk.AsteriskConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.response.ManagerResponse;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Сервис для инициации исходящих вызовов через Asterisk AMI.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OriginateService {

    private final AsteriskConnection asteriskConnection;

    /**
     * Инициировать вызов на оператора.
     * @param extension Внутренний номер оператора (1001, 1002)
     * @param callerId Отображаемый номер/имя
     * @return Результат операции
     */
    public Map<String, Object> originateCall(String extension, String callerId) {
        try {
            if (!asteriskConnection.isConnected()) {
                log.error("Asterisk не подключён");
                return Map.of("status", "error", "message", "Asterisk не подключён");
            }

            ManagerConnection connection = asteriskConnection.getConnection();

            // Формируем номер в формате SIP/1001
            String channel = "SIP/" + extension;

            OriginateAction originateAction = new OriginateAction();
            originateAction.setChannel(channel);
            originateAction.setContext("operators");
            originateAction.setExten(extension);  // Номер оператора
            originateAction.setPriority(1);
            originateAction.setTimeout(30000);    // 30 секунд
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