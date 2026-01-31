package local.nca.callcenter.domain.service;

/**
 * Интерфейс мониторинга очереди колл-центра.
 * Соответствует диаграмме классов из лабораторных работ №3-4.
 * Подготавливает проект к тестированию в лабораторной работе №6.
 */
public interface QueueMonitoringService {
    boolean isQueueActive(String queueName);
    int getCallCountInQueue(String queueName);
    boolean isOperatorAvailable(String operatorId);
}