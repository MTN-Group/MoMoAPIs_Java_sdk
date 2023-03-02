package com.momo.api.requests.remittance;

import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.model.StatusResponse;
import com.momo.api.constants.NotificationType;
import com.momo.api.models.Transfer;

/**
 *
 * interface RemittanceRequestWithCallBackUrl
 */
public interface RemittanceRequestWithCallBackUrl {

    public StatusResponse transfer(Transfer transfer) throws MoMoException;

    public RemittanceRequestWithCallBackUrl addCallBackUrl(final String callBackURL);

    public RemittanceRequestWithCallBackUrl setNotificationType(final NotificationType notificationType);
}
