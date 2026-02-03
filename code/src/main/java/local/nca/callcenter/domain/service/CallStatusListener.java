package local.nca.callcenter.domain.service;

import local.nca.callcenter.domain.model.Call;
import local.nca.callcenter.domain.model.CallStatus;

/**
 * Слушатель изменений статуса вызова.
 */
public interface CallStatusListener {

    /**
     * Вызов изменил статус.
     * @param call вызов
     * @param oldStatus старый статус
     * @param newStatus новый статус
     */
    void onCallStatusChanged(Call call, CallStatus oldStatus, CallStatus newStatus);
}