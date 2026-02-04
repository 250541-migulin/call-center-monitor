package local.nca.callcenter.asterisk.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * Конфигурационные свойства для подключения к Asterisk AMI.
 * Использует префикс "asterisk.ami" для всех свойств.
 * <p>
 * Пример использования в application.properties:
 * asterisk.ami.host=localhost
 * asterisk.ami.port=5038
 * asterisk.ami.username=admin
 * asterisk.ami.password=secret
 * asterisk.ami.queue-name=support
 */
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "asterisk.ami")
@Validated
public class AsteriskProperties {

    /**
     * Хост сервера Asterisk AMI.
     * По умолчанию: localhost
     */
    @NotBlank(message = "Хост Asterisk AMI не может быть пустым")
    private String host = "localhost";

    /**
     * Порт сервера Asterisk AMI.
     * По умолчанию: 5038 (стандартный порт AMI)
     */
    @NotNull(message = "Порт Asterisk AMI не может быть null")
    @Positive(message = "Порт Asterisk AMI должен быть положительным числом")
    private Integer port = 5038;

    /**
     * Имя пользователя для подключения к Asterisk AMI.
     * Обязательное поле.
     */
    @NotBlank(message = "Имя пользователя Asterisk AMI не может быть пустым")
    private String username;

    /**
     * Пароль для подключения к Asterisk AMI.
     * Обязательное поле.
     */
    @NotBlank(message = "Пароль Asterisk AMI не может быть пустым")
    private String password;

    /**
     * Имя очереди колл-центра для мониторинга.
     * По умолчанию: support
     */
    @NotBlank(message = "Имя очереди Asterisk не может быть пустым")
    private String queueName = "support";

    /**
     * Таймаут подключения в миллисекундах.
     * По умолчанию: 5000 (5 секунд)
     */
    @Positive(message = "Таймаут подключения должен быть положительным")
    private Integer connectionTimeout = 5000;

    /**
     * Включено ли автоматическое переподключение при разрыве соединения.
     * По умолчанию: true
     */
    private boolean autoReconnect = true;

    /**
     * Интервал переподключения в миллисекундах (если включено).
     * По умолчанию: 10000 (10 секунд)
     */
    @Positive(message = "Интервал переподключения должен быть положительным")
    private Integer reconnectInterval = 10000;

    /**
     * Логирование конфигурации при инициализации (без пароля).
     */
    public void logConfiguration() {
        log.info("Конфигурация Asterisk AMI загружена:");
        log.info("  Хост: {}", host);
        log.info("  Порт: {}", port);
        log.info("  Пользователь: {}", username);
        log.info("  Очередь: {}", queueName);
        log.info("  Таймаут подключения: {} мс", connectionTimeout);
        log.info("  Автопереподключение: {}", autoReconnect);
        log.info("  Интервал переподключения: {} мс", reconnectInterval);
    }
}