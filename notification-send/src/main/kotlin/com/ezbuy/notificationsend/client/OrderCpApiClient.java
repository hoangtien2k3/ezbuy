package com.ezbuy.notificationsend.client;

import com.ezbuy.notificationmodel.dto.Result;
import com.reactify.exception.BusinessException;

public interface OrderCpApiClient {

    Result wsCpMt(String cpCode, String userID, String receiverID, String serviceID, String content) throws BusinessException, Exception;

}
