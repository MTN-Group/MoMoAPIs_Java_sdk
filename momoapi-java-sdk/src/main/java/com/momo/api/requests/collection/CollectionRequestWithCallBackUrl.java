package com.momo.api.requests.collection;

import com.momo.api.base.constants.AccessType;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.model.BCAuthorize;
import com.momo.api.base.model.StatusResponse;
import com.momo.api.constants.NotificationType;
import com.momo.api.models.AccountHolder;
import com.momo.api.models.UserInfo;
import com.momo.api.models.collection.RequestPay;
import com.momo.api.models.collection.Withdraw;

/**
 *
 * interface CollectionRequestWithCallBackUrl
 */
public interface CollectionRequestWithCallBackUrl {

    public StatusResponse requestToPay(RequestPay requestPay) throws MoMoException;

    public StatusResponse requestToWithdrawV1(Withdraw withdraw) throws MoMoException;

    public StatusResponse requestToWithdrawV2(Withdraw withdraw) throws MoMoException;

    public BCAuthorize bcAuthorize(AccountHolder accountHolder, String scope, AccessType accesType) throws MoMoException;

    public CollectionRequestWithCallBackUrl addCallBackUrl(final String callBackURL);

    public CollectionRequestWithCallBackUrl setNotificationType(final NotificationType notificationType);
}
