package com.momo.api.requests.remittance;

import com.momo.api.base.constants.*;
import com.momo.api.base.context.remittance.RemittanceContext;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.model.BCAuthorize;
import com.momo.api.base.model.HttpErrorResponse;
import com.momo.api.base.model.StatusResponse;
import com.momo.api.base.util.StringUtils;
import com.momo.api.constants.NotificationType;
import com.momo.api.models.*;
import com.momo.api.requests.TransferRequest;

/**
 *
 * Class RemittanceRequest
 */
public class RemittanceRequest extends TransferRequest implements RemittanceRequestWithCallBackUrl {

    protected NotificationType notificationType = NotificationType.CALLBACK;
    protected String callBackURL;

    /**
     * Transfer operation is used to transfer an amount from the own account to
     * a payee account. Status of the transaction can validated by using the
     * getTransferStatus(String referenceId) request method.
     *
     * @param transfer
     * @return
     * @throws MoMoException
     */
    @Override
    public StatusResponse transfer(Transfer transfer) throws MoMoException {
        return transfer(SubscriptionType.REMITTANCE, RemittanceContext.getContext(), transfer, this.notificationType, this.callBackURL);
    }

    /**
     * This operation is used to get the status of a transfer. "referenceId"
     * that was created during transfer method is passed in as the parameter to
     * this request method.
     *
     * @param referenceId
     * @return
     * @throws MoMoException
     */
    public TransferStatus getTransferStatus(String referenceId) throws MoMoException {
        return getTransferStatus(referenceId, SubscriptionType.REMITTANCE, RemittanceContext.getContext());
    }

    /**
     * This operation is used to check if an account holder is registered and
     * active in the system.
     *
     * @param accountHolder
     * @return
     * @throws MoMoException
     */
    public Result validateAccountHolderStatus(AccountHolder accountHolder) throws MoMoException {
        return validateAccountHolderStatus(accountHolder, SubscriptionType.REMITTANCE, RemittanceContext.getContext());
    }

    /**
     * Get the balance of the account.
     *
     * @return @throws MoMoException
     */
    public AccountBalance getAccountBalance() throws MoMoException {
        return getAccountBalance(SubscriptionType.REMITTANCE, RemittanceContext.getContext());
    }

    /**
     * This operation returns personal information of the account holder. The
     * operation does not need any consent by the account holder.
     *
     * @param msisdn
     * @return
     * @throws MoMoException
     */
    public BasicUserInfo getBasicUserinfo(String msisdn) throws MoMoException {
        return getBasicUserinfo(msisdn, SubscriptionType.REMITTANCE, RemittanceContext.getContext());
    }

    /**
     * This operation is used to send additional Notification to an End User.
     *
     * @param referenceId
     * @param deliveryNotification
     * @return
     * @throws MoMoException
     */
    public StatusResponse requestToPayDeliveryNotification(String referenceId, DeliveryNotification deliveryNotification) throws MoMoException {
        return requestToPayDeliveryNotification(referenceId, deliveryNotification, null, null, SubscriptionType.REMITTANCE, RemittanceContext.getContext());
    }

    /**
     * This operation is used to send additional Notification to an End User.The notification message is also passed in as a header.
     *
     * @param referenceId
     * @param deliveryNotification
     * @param deliveryNotificationHeader
     * @param language
     * @return
     * @throws MoMoException
     */
    public StatusResponse requestToPayDeliveryNotification(String referenceId, DeliveryNotification deliveryNotification, String deliveryNotificationHeader, String language) throws MoMoException {
        return requestToPayDeliveryNotification(referenceId, deliveryNotification, deliveryNotificationHeader, language, SubscriptionType.REMITTANCE, RemittanceContext.getContext());
    }

    /**
     * This operation is used to get the details of the account holder for the
     * requested scopes.
     *
     * @param accountHolder
     * @param scope
     * @param accessType
     * @return
     * @throws MoMoException
     */
    @Override
    public UserInfo getUserInfoWithConsent(AccountHolder accountHolder, String scope, AccessType accessType) throws MoMoException {
        return getUserInfoWithConsent(accountHolder, scope, accessType, SubscriptionType.REMITTANCE, RemittanceContext.getContext(), this.notificationType, this.callBackURL);
    }

    /**
     * This callBackURL will have higher priority and will override the
     * callBackURL set for the Context
     *
     * @param callBackURL
     * @return
     */
    @Override
    public RemittanceRequest addCallBackUrl(final String callBackURL) {
        this.callBackURL = callBackURL;
        if (!StringUtils.isNullOrEmpty(callBackURL)) {
            return setNotificationType(NotificationType.CALLBACK);
        } else {
            return this;
        }
    }

    /**
     * NotificationType can be set to CALLBACK or POLLING. If it is CALLBACK, a
     * response will be sent to the callBackURL set for the RemittanceRequest
     * object or for the RemittanceConfiguration . If it is POLLING, no response
     * will be sent to the callBackURL
     *
     * @param notificationType
     * @return
     */
    @Override
    public RemittanceRequest setNotificationType(final NotificationType notificationType) {
        this.notificationType = notificationType;
        return this;
    }

}
