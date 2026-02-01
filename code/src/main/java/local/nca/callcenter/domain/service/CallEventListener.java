package local.nca.callcenter.domain.service;

import local.nca.callcenter.domain.model.Call;

/**
 * Слушатель событий вызовов в очереди колл-центра.
 * Реализует паттерн Observer для реакции на события очереди.
 */
public interface CallEventListener {

    /**
     * Обработка события: новый вызов поступил в очередь.
     * @param call объект вызова
     */
    void onCallEntered(Call call);

    /**
     * Обработка события: вызов покинул очередь.
     * @param callId идентификатор вызова
     */
    void onCallLeft(String callId);
}