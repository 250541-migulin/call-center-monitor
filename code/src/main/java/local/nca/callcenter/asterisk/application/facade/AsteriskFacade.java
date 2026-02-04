package local.nca.callcenter.asterisk.application.facade;

import local.nca.callcenter.asterisk.application.service.OriginateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Map;

/**
 * ФАСАД Asterisk AMI.
 * Единая точка входа для бизнес-логики.
 * Скрывает все технические детали (OriginateAction, ManagerConnection).
 */
@Service
@RequiredArgsConstructor
public class AsteriskFacade {

    private final OriginateService originateService;

    /**
     * Инициировать исходящий вызов.
     * @param extension Внутренний номер оператора (1001, 1002)
     * @param callerId Отображаемый номер/имя
     * @return Результат операции
     */
    public Map<String, Object> makeCall(String extension, String callerId) {
        return originateService.originateCall(extension, callerId);
    }

    /**
     * Проверить подключение к Asterisk.
     */
    public boolean isConnected() {
        return originateService.isConnected(); // ← Добавить этот метод в OriginateService
    }
}