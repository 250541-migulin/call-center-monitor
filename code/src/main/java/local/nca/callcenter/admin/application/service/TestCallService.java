// admin/application/TestCallService.java
package local.nca.callcenter.admin.application.service;

import local.nca.callcenter.asterisk.application.facade.AsteriskFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Сервис для административных операций с вызовами.
 *
 * Использует фасад AsteriskFacade для инициации тестовых вызовов.
 * Не знает про технические детали Asterisk (OriginateAction, ManagerConnection).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TestCallService {

    private final AsteriskFacade asteriskFacade;

    /**
     * Инициировать тестовый вызов на оператора.
     *
     * @param extension внутренний номер оператора (например: "1001")
     * @return результат операции
     */
    public Map<String, Object> makeTestCall(String extension) {
        log.info("🧪 Админ инициирует тестовый вызов на {}", extension);

        return asteriskFacade.makeCall(extension, "ТЕСТОВЫЙ ВЫЗОВ");
    }

    public boolean checkAsteriskConnection() {
        return asteriskFacade.isConnected();
    }
}