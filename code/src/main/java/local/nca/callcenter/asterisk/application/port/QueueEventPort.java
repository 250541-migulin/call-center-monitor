package local.nca.callcenter.asterisk.application.port;

/**
 * ВХОДЯЩИЙ ПОРТ: События из очереди Asterisk.
 */
public interface QueueEventPort {

    /**
     * Вызов поступил в очередь.
     * @param uniqueId технический ID из Asterisk (например, "12345.67890")
     * @param callerId номер звонящего ("+79991234567")
     * @param queueName имя очереди ("support")
     */
    void onCallEntered(String uniqueId, String callerId, String queueName);

    /**
     * Вызов покинул очередь.
     * @param uniqueId технический ID из Asterisk
     */
    void onCallLeft(String uniqueId);
}