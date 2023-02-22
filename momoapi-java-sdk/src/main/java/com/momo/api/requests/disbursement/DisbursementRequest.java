package com.momo.api.requests.disbursement;

import com.momo.api.base.constants.API;
import com.momo.api.base.constants.AccessType;
import com.momo.api.base.constants.Constants;
import com.momo.api.base.constants.HttpMethod;
import com.momo.api.base.constants.SubscriptionType;
import com.momo.api.base.context.MoMoContext;
import com.momo.api.base.context.disbursement.DisbursementContext;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.model.BCAuthorize;
import com.momo.api.base.model.StatusResponse;
import com.momo.api.base.model.HttpErrorResponse;
import com.momo.api.base.util.JSONFormatter;
import com.momo.api.base.util.StringUtils;
import com.momo.api.constants.NotificationType;
import com.momo.api.constants.RequestType;
import com.momo.api.models.AccountBalance;
import com.momo.api.models.AccountHolder;
import com.momo.api.models.BasicUserInfo;
import com.momo.api.models.DeliveryNotification;
import com.momo.api.models.Result;
import com.momo.api.models.Transfer;
import com.momo.api.models.disbursement.Deposit;
import com.momo.api.models.disbursement.DepositStatus;
import com.momo.api.models.disbursement.Refund;
import com.momo.api.models.disbursement.RefundStatus;
import com.momo.api.models.TransferStatus;
import com.momo.api.requests.TransferRequest;
import java.util.UUID;

/**
 *
 * Class DisbursementRequest
 */
public class DisbursementRequest extends TransferRequest {

    /**
     * Transfer operation is used to transfer an amount from the own account to
     * a payee account. Status of the transaction can validated by using the
     * getTransferStatus(String referenceId) request method.
     *
     * @param transfer
     * @return
     * @throws MoMoException
     */
    public StatusResponse transfer(Transfer transfer) throws MoMoException {
        return transfer(SubscriptionType.DISBURSEMENT, DisbursementContext.getContext(), transfer);
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
        return getTransferStatus(referenceId, SubscriptionType.DISBURSEMENT, DisbursementContext.getContext());
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
        return validateAccountHolderStatus(accountHolder, SubscriptionType.DISBURSEMENT, DisbursementContext.getContext());
    }

    /**
     * Get the balance of the account.
     *
     * @return @throws MoMoException
     */
    public AccountBalance getAccountBalance() throws MoMoException {
        return getAccountBalance(SubscriptionType.DISBURSEMENT, DisbursementContext.getContext());
    }

    /**
     * Get the balance of the account. Currency is passed in as the parameter.
     *
     * @param currency
     * @return
     * @throws MoMoException
     */
    public AccountBalance getAccountBalanceInSpecificCurrency(String currency) throws MoMoException {
        return getAccountBalanceInSpecificCurrency(currency, SubscriptionType.DISBURSEMENT, DisbursementContext.getContext());
    }
    
    /**
     *
     * @param currency
     * @param subscriptionType
     * @param currentContext
     * @return
     * @throws MoMoException
     */
    private AccountBalance getAccountBalanceInSpecificCurrency(String currency, String subscriptionType, MoMoContext currentContext) throws MoMoException {
        if (StringUtils.isNullOrEmpty(currency)) {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.INTERNAL_ERROR_CATEGORY,
                            Constants.GENERIC_ERROR_CODE).errorDescription(
                            currency == null ? Constants.NULL_VALUE_ERROR : Constants.EMPTY_STRING_ERROR).build()
            );
        }

        String resourcePath = API.SUBSCRIPTION_VER_ACCOUNT_BALANCE_CURRENCY
                .replace(Constants.SUBSCRIPTION_TYPE, subscriptionType)
                .replace(Constants.CURRENCY, currency);
        return createRequest(HttpMethod.GET, resourcePath, null, notificationType, callBackURL, AccountBalance.class, currentContext);
    }

    /**
     * Deposit operation is used to deposit an amount from the owner’s account
     * to a payee account. Status of the transaction can be validated by using
     * the getDepositStatus(String referenceId) request method.
     *
     * @param deposit
     * @return
     * @throws MoMoException
     */
    public StatusResponse depositV1(Deposit deposit) throws MoMoException {
        if (deposit == null) {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.VALIDATION_ERROR_CATEGORY,
                            Constants.VALUE_NOT_SUPPLIED_ERROR_CODE)
                            .errorDescription(Constants.DEPOSIT_OBJECT_INIT_ERROR).build());
        }

        this.referenceId = UUID.randomUUID().toString();
        DisbursementContext.getContext().getHTTPHeaders()
                .put(Constants.X_REFERENCE_ID, this.referenceId);

        String resourcePath = API.SUBSCRIPTION_VER_REQUEST
                .replace(Constants.SUBSCRIPTION_TYPE, SubscriptionType.DISBURSEMENT)
                .replace(Constants.REQUEST_TYPE, RequestType.DEPOSIT);
        StatusResponse statusResponse = createRequest(HttpMethod.POST, resourcePath, JSONFormatter.toJSON(deposit), notificationType, callBackURL, DisbursementContext.getContext());
        return statusResponse;
    }

    /**
     * Deposit operation is used to deposit an amount from the owner’s account
     * to a payee account. Status of the transaction can be validated by using
     * the getDepositStatus(String referenceId) request method.
     *
     * @param deposit
     * @return
     * @throws MoMoException
     */
    public StatusResponse depositV2(Deposit deposit) throws MoMoException {
        if (deposit == null) {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.VALIDATION_ERROR_CATEGORY,
                            Constants.VALUE_NOT_SUPPLIED_ERROR_CODE)
                            .errorDescription(Constants.DEPOSIT_OBJECT_INIT_ERROR).build());
        }

        this.referenceId = UUID.randomUUID().toString();
        DisbursementContext.getContext().getHTTPHeaders()
                .put(Constants.X_REFERENCE_ID, this.referenceId);

        String resourcePath = API.SUBSCRIPTION_VER2_REQUEST
                .replace(Constants.SUBSCRIPTION_TYPE, SubscriptionType.DISBURSEMENT)
                .replace(Constants.REQUEST_TYPE, RequestType.DEPOSIT);
        StatusResponse statusResponse = createRequest(HttpMethod.POST, resourcePath, JSONFormatter.toJSON(deposit), notificationType, callBackURL, DisbursementContext.getContext());
        return statusResponse;
    }

    /**
     * This operation is used to get the status of a deposit. "referenceId" that
     * was created during deposit method is passed in as the parameter to this
     * request method.
     *
     * @param referenceId
     * @return
     * @throws MoMoException
     */
    public DepositStatus getDepositStatus(String referenceId) throws MoMoException {
        if (StringUtils.isNullOrEmpty(referenceId)) {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.INTERNAL_ERROR_CATEGORY,
                            Constants.GENERIC_ERROR_CODE).errorDescription(referenceId == null ? Constants.NULL_VALUE_ERROR : Constants.EMPTY_STRING_ERROR).build());
        }

        String resourcePath = API.SUBSCRIPTION_VER_REQUEST_REFERENCE_ID
                .replace(Constants.SUBSCRIPTION_TYPE, SubscriptionType.DISBURSEMENT)
                .replace(Constants.REQUEST_TYPE, RequestType.DEPOSIT)
                .replace(Constants.REFERENCE_ID, referenceId);
        return createRequest(HttpMethod.GET, resourcePath, null, notificationType, callBackURL, DepositStatus.class, DisbursementContext.getContext());
    }

    /**
     * Refund operation is used to refund an amount from the owner’s account to
     * a payee account. Status of the transaction can validated by using the
     * getRefundStatus(String referenceId) request method.
     *
     * @param refund
     * @return
     * @throws MoMoException
     */
    public StatusResponse refundV1(Refund refund) throws MoMoException {
        if (refund == null) {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.VALIDATION_ERROR_CATEGORY,
                            Constants.VALUE_NOT_SUPPLIED_ERROR_CODE)
                            .errorDescription(Constants.REFUND_OBJECT_INIT_ERROR).build());
        }

        this.referenceId = UUID.randomUUID().toString();
        DisbursementContext.getContext().getHTTPHeaders()
                .put(Constants.X_REFERENCE_ID, this.referenceId);

        String resourcePath = API.SUBSCRIPTION_VER_REQUEST
                .replace(Constants.SUBSCRIPTION_TYPE, SubscriptionType.DISBURSEMENT)
                .replace(Constants.REQUEST_TYPE, RequestType.REFUND);
        StatusResponse statusResponse = createRequest(HttpMethod.POST, resourcePath, JSONFormatter.toJSON(refund), notificationType, callBackURL, DisbursementContext.getContext());
        return statusResponse;
    }

    /**
     * Refund operation is used to refund an amount from the owner’s account to
     * a payee account. Status of the transaction can validated by using the
     * getRefundStatus(String referenceId) request method.
     *
     * @param refund
     * @return
     * @throws MoMoException
     */
    public StatusResponse refundV2(Refund refund) throws MoMoException {
        if (refund == null) {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.VALIDATION_ERROR_CATEGORY,
                            Constants.VALUE_NOT_SUPPLIED_ERROR_CODE)
                            .errorDescription(Constants.REFUND_OBJECT_INIT_ERROR).build());
        }

        this.referenceId = UUID.randomUUID().toString();
        DisbursementContext.getContext().getHTTPHeaders()
                .put(Constants.X_REFERENCE_ID, this.referenceId);

        String resourcePath = API.SUBSCRIPTION_VER2_REQUEST
                .replace(Constants.SUBSCRIPTION_TYPE, SubscriptionType.DISBURSEMENT)
                .replace(Constants.REQUEST_TYPE, RequestType.REFUND);
        StatusResponse statusResponse = createRequest(HttpMethod.POST, resourcePath, JSONFormatter.toJSON(refund), notificationType, callBackURL, DisbursementContext.getContext());
        return statusResponse;
    }

    /**
     * This operation is used to get the status of a refund. "referenceId" that
     * was created during refund method is passed in as the parameter to this
     * request method.
     *
     * @param referenceId
     * @return
     * @throws MoMoException
     */
    public RefundStatus getRefundStatus(String referenceId) throws MoMoException {
        if (StringUtils.isNullOrEmpty(referenceId)) {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.INTERNAL_ERROR_CATEGORY,
                            Constants.GENERIC_ERROR_CODE).errorDescription(referenceId == null ? Constants.NULL_VALUE_ERROR : Constants.EMPTY_STRING_ERROR).build());
        }

        String resourcePath = API.SUBSCRIPTION_VER_REQUEST_REFERENCE_ID
                .replace(Constants.SUBSCRIPTION_TYPE, SubscriptionType.DISBURSEMENT)
                .replace(Constants.REQUEST_TYPE, RequestType.REFUND)
                .replace(Constants.REFERENCE_ID, referenceId);
        return createRequest(HttpMethod.GET, resourcePath, null, notificationType, callBackURL, RefundStatus.class, DisbursementContext.getContext());
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
        return requestToPayDeliveryNotification(referenceId, deliveryNotification, null, false, SubscriptionType.DISBURSEMENT, DisbursementContext.getContext());
    }

    /**
     * This operation is used to send additional Notification to an End User.
     * The notification message is also passed in as a header.
     *
     * @param referenceId
     * @param deliveryNotification
     * @param deliveryNotificationHeader
     * @return
     * @throws MoMoException
     */
    public StatusResponse requestToPayDeliveryNotification(String referenceId, DeliveryNotification deliveryNotification, String deliveryNotificationHeader) throws MoMoException {
        return requestToPayDeliveryNotification(referenceId, deliveryNotification, deliveryNotificationHeader, true, SubscriptionType.DISBURSEMENT, DisbursementContext.getContext());
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
        return getBasicUserinfo(msisdn, SubscriptionType.DISBURSEMENT, DisbursementContext.getContext());
    }

    /**
     * This operation is used to claim a consent by the account holder for the
     * requested scopes.bCAuthorize receives a parameter "auth_req_id" which is
     * passed into Oauth2 API which is then used in getUserInfoWithConsent API
     *
     * @param accountHolder
     * @param scope
     * @param access_type
     * @return
     * @throws MoMoException
     */
    public BCAuthorize bCAuthorize(AccountHolder accountHolder, String scope, AccessType access_type) throws MoMoException {
        return bCAuthorize(accountHolder, scope, access_type, SubscriptionType.DISBURSEMENT, DisbursementContext.getContext());
    }

    /**
     * This callBackURL will have higher priority and will override the
     * callBackURL set for the Context
     *
     * @param callBackURL
     * @return
     */
    public DisbursementRequest addCallBackUrl(final String callBackURL) {
        this.callBackURL = callBackURL;
        if(!StringUtils.isNullOrEmpty(callBackURL)){
            return setNotificationType(NotificationType.CALLBACK);
        } else {
            return this;
        }
    }

    /**
     * NotificationType can be set to CALLBACK or POLLING. If it is CALLBACK, a
     * response will be sent to the callBackURL set for the DisbursementRequest
     * object or for the DisbursementConfiguration . If it is POLLING, no response
     * will be sent to the callBackURL
     *
     * @param notificationType
     * @return
     */
    public DisbursementRequest setNotificationType(final NotificationType notificationType) {
        this.notificationType = notificationType;
        return this;
    }

}
