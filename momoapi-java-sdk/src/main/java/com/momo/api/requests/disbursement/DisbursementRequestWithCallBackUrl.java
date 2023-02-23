package com.momo.api.requests.disbursement;

import com.momo.api.base.constants.AccessType;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.model.BCAuthorize;
import com.momo.api.base.model.StatusResponse;
import com.momo.api.constants.NotificationType;
import com.momo.api.models.AccountHolder;
import com.momo.api.models.Transfer;
import com.momo.api.models.disbursement.Deposit;
import com.momo.api.models.disbursement.Refund;

/**
 *
 * interface DisbursementRequestWithCallBackUrl
 */
public interface DisbursementRequestWithCallBackUrl {

    public StatusResponse transfer(Transfer transfer) throws MoMoException;

    public StatusResponse depositV1(Deposit deposit) throws MoMoException;

    public StatusResponse depositV2(Deposit deposit) throws MoMoException;
    
    public StatusResponse refundV1(Refund refund) throws MoMoException;
    
    public StatusResponse refundV2(Refund refund) throws MoMoException;

    public BCAuthorize bcAuthorize(AccountHolder accountHolder, String scope, AccessType accesType) throws MoMoException;

    public DisbursementRequestWithCallBackUrl addCallBackUrl(final String callBackURL);

    public DisbursementRequestWithCallBackUrl setNotificationType(final NotificationType notificationType);
}
