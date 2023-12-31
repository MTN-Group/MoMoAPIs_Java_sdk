package com.momo.api.requests.collection;

import com.momo.api.base.constants.API;
import com.momo.api.base.constants.AccessType;
import com.momo.api.base.constants.Constants;
import com.momo.api.base.constants.HttpMethod;
import com.momo.api.base.constants.SubscriptionType;
import com.momo.api.base.context.MoMoContext;
import com.momo.api.base.context.collection.CollectionContext;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.model.BCAuthorize;
import com.momo.api.base.model.StatusResponse;
import com.momo.api.base.model.HttpErrorResponse;
import com.momo.api.base.util.JSONFormatter;
import com.momo.api.base.util.StringUtils;
import com.momo.api.constants.NotificationType;
import com.momo.api.models.collection.RequestPayStatus;
import com.momo.api.constants.RequestType;
import com.momo.api.models.AccountBalance;
import com.momo.api.models.AccountHolder;
import com.momo.api.models.BasicUserInfo;
import com.momo.api.models.DeliveryNotification;
import com.momo.api.models.Result;
import com.momo.api.models.UserInfo;
import com.momo.api.models.collection.RequestPay;
import com.momo.api.models.collection.Withdraw;
import com.momo.api.models.collection.WithdrawStatus;
import com.momo.api.requests.CommonRequest;
import java.util.UUID;

/**
 *
 * Class CollectionRequest
 */
public class CollectionRequest extends CommonRequest implements CollectionRequestWithCallBackUrl {

    protected NotificationType notificationType = NotificationType.CALLBACK;
    protected String callBackURL;

    /**
     * This operation is used to request a payment from a consumer (Payer). The
     * payer will be asked to authorize the payment. The transaction will be
     * executed once the payer has authorized the payment. The requestToPay will
     * be in status PENDING until the transaction is authorized or declined by
     * the payer or it is timed out by the system. Status of the transaction can
     * be validated by using the requestToPayTransactionStatus(String
     * referenceId) request method
     *
     * @param requestPay
     * @return
     * @throws MoMoException
     */
    @Override
    public StatusResponse requestToPay(RequestPay requestPay) throws MoMoException {
        if (requestPay == null) {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.VALIDATION_ERROR_CATEGORY,
                            Constants.VALUE_NOT_SUPPLIED_ERROR_CODE)
                            .errorDescription(Constants.REQUEST_TO_PAY_OBJECT_INIT_ERROR).build());
        }

        this.referenceId = UUID.randomUUID().toString();
        CollectionContext.getContext().getHTTPHeaders()
                .put(Constants.X_REFERENCE_ID, this.referenceId);

        String resourcePath = API.SUBSCRIPTION_VER_REQUEST
                .replace(Constants.SUBSCRIPTION_TYPE, SubscriptionType.COLLECTION)
                .replace(Constants.REQUEST_TYPE, RequestType.REQUEST_TO_PAY);
        StatusResponse statusResponse = createRequest(HttpMethod.POST, resourcePath, JSONFormatter.toJSON(requestPay), notificationType, callBackURL, CollectionContext.getContext());
        if(CollectionContext.getContext().getHTTPHeaders().containsKey(Constants.X_REFERENCE_ID)){
            CollectionContext.getContext().getHTTPHeaders().remove(Constants.X_REFERENCE_ID);
        }
        return statusResponse;
    }

    /**
     * This operation is used to get the status of a request to pay.
     * "referenceId" that was created during requestToPay method is passed in as
     * the parameter to this request method.
     *
     * @param referenceId
     * @return
     * @throws MoMoException
     */
    public RequestPayStatus requestToPayTransactionStatus(String referenceId) throws MoMoException {
        if (StringUtils.isNullOrEmpty(referenceId)) {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.INTERNAL_ERROR_CATEGORY,
                            Constants.GENERIC_ERROR_CODE).errorDescription(referenceId == null ? Constants.NULL_VALUE_ERROR : Constants.EMPTY_STRING_ERROR).build());
        }
        String resourcePath = API.SUBSCRIPTION_VER_REQUEST_REFERENCE_ID
                .replace(Constants.SUBSCRIPTION_TYPE, SubscriptionType.COLLECTION)
                .replace(Constants.REQUEST_TYPE, RequestType.REQUEST_TO_PAY)
                .replace(Constants.REFERENCE_ID, referenceId);
        return createRequest(HttpMethod.GET, resourcePath, null, NotificationType.POLLING, null, RequestPayStatus.class, CollectionContext.getContext());

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
        return requestToPayDeliveryNotification(referenceId, deliveryNotification, null, null, SubscriptionType.COLLECTION, CollectionContext.getContext());
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
        return requestToPayDeliveryNotification(referenceId, deliveryNotification, deliveryNotificationHeader, language, SubscriptionType.COLLECTION, CollectionContext.getContext());
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
        return validateAccountHolderStatus(accountHolder, SubscriptionType.COLLECTION, CollectionContext.getContext());
    }

    /**
     * Get the balance of the account.
     *
     * @return @throws MoMoException
     */
    public AccountBalance getAccountBalance() throws MoMoException {
        return getAccountBalance(SubscriptionType.COLLECTION, CollectionContext.getContext());
    }

    /**
     * Get the balance of the account. Currency is passed in as the parameter.
     *
     * @param currency
     * @return
     * @throws MoMoException
     */
    public AccountBalance getAccountBalanceInSpecificCurrency(String currency) throws MoMoException {
        return getAccountBalanceInSpecificCurrency(currency, SubscriptionType.COLLECTION, CollectionContext.getContext());
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
        return createRequest(HttpMethod.GET, resourcePath, null, NotificationType.POLLING, null, AccountBalance.class, currentContext);
    }

    /**
     * This operation is used to request a withdrawal (cash-out) from a consumer
     * (Payer). The payer will be asked to authorize the withdrawal. The
     * transaction will be executed once the payer has authorized the withdrawal
     *
     * @param withdraw
     * @return
     * @throws MoMoException
     */
    @Override
    public StatusResponse requestToWithdrawV1(Withdraw withdraw) throws MoMoException {
        if (withdraw == null) {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.VALIDATION_ERROR_CATEGORY,
                            Constants.VALUE_NOT_SUPPLIED_ERROR_CODE)
                            .errorDescription(Constants.WITHDRAW_OBJECT_INIT_ERROR).build());
        }

        this.referenceId = UUID.randomUUID().toString();
        CollectionContext.getContext().getHTTPHeaders()
                .put(Constants.X_REFERENCE_ID, this.referenceId);

        String resourcePath = API.SUBSCRIPTION_VER_REQUEST
                .replace(Constants.SUBSCRIPTION_TYPE, SubscriptionType.COLLECTION)
                .replace(Constants.REQUEST_TYPE, RequestType.REQUEST_TO_WITHDRAW);
        StatusResponse statusResponse = createRequest(HttpMethod.POST, resourcePath, JSONFormatter.toJSON(withdraw), notificationType, callBackURL, CollectionContext.getContext());
        if(CollectionContext.getContext().getHTTPHeaders().containsKey(Constants.X_REFERENCE_ID)){
            CollectionContext.getContext().getHTTPHeaders().remove(Constants.X_REFERENCE_ID);
        }
        return statusResponse;
    }

    /**
     * This operation is used to request a withdrawal (cash-out) from a consumer
     * (Payer). The payer will be asked to authorize the withdrawal. The
     * transaction will be executed once the payer has authorized the withdrawal
     *
     * @param withdraw
     * @return
     * @throws MoMoException
     */
    @Override
    public StatusResponse requestToWithdrawV2(Withdraw withdraw) throws MoMoException {
        if (withdraw == null) {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.VALIDATION_ERROR_CATEGORY,
                            Constants.VALUE_NOT_SUPPLIED_ERROR_CODE)
                            .errorDescription(Constants.WITHDRAW_OBJECT_INIT_ERROR).build());
        }

        this.referenceId = UUID.randomUUID().toString();
        CollectionContext.getContext().getHTTPHeaders()
                .put(Constants.X_REFERENCE_ID, this.referenceId);

        String resourcePath = API.SUBSCRIPTION_VER2_REQUEST
                .replace(Constants.SUBSCRIPTION_TYPE, SubscriptionType.COLLECTION)
                .replace(Constants.REQUEST_TYPE, RequestType.REQUEST_TO_WITHDRAW);
        StatusResponse statusResponse = createRequest(HttpMethod.POST, resourcePath, JSONFormatter.toJSON(withdraw), notificationType, callBackURL, CollectionContext.getContext());
        if(CollectionContext.getContext().getHTTPHeaders().containsKey(Constants.X_REFERENCE_ID)){
            CollectionContext.getContext().getHTTPHeaders().remove(Constants.X_REFERENCE_ID);
        }
        return statusResponse;
    }

    /**
     * This operation is used to get the status of a request to withdraw.
     * "referenceId" that was created during transfer method is passed in as the
     * parameter to this request method.
     *
     * @param referenceId
     * @return
     * @throws MoMoException
     */
    public WithdrawStatus requestToWithdrawTransactionStatus(String referenceId) throws MoMoException {
        if (StringUtils.isNullOrEmpty(referenceId)) {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.INTERNAL_ERROR_CATEGORY,
                            Constants.GENERIC_ERROR_CODE).errorDescription(referenceId == null ? Constants.NULL_VALUE_ERROR : Constants.EMPTY_STRING_ERROR).build());
        }
        String resourcePath = API.SUBSCRIPTION_VER_REQUEST_REFERENCE_ID
                .replace(Constants.SUBSCRIPTION_TYPE, SubscriptionType.COLLECTION)
                .replace(Constants.REQUEST_TYPE, RequestType.REQUEST_TO_WITHDRAW)
                .replace(Constants.REFERENCE_ID, referenceId);
        return createRequest(HttpMethod.GET, resourcePath, null, NotificationType.POLLING, null, WithdrawStatus.class, CollectionContext.getContext());
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
        return getBasicUserinfo(msisdn, SubscriptionType.COLLECTION, CollectionContext.getContext());
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
        return getUserInfoWithConsent(accountHolder, scope, accessType, SubscriptionType.COLLECTION, CollectionContext.getContext(), this.notificationType, this.callBackURL);
    }

    /**
     * This callBackURL will have higher priority and will override the
     * callBackURL set for the Context
     *
     * @param callBackURL
     * @return
     */
    @Override
    public CollectionRequestWithCallBackUrl addCallBackUrl(final String callBackURL) {
        this.callBackURL = callBackURL;
        if (!StringUtils.isNullOrEmpty(callBackURL)) {
            return setNotificationType(NotificationType.CALLBACK);
        } else {
            return this;
        }
    }

    /**
     * NotificationType can be set to CALLBACK or POLLING. If it is CALLBACK, a
     * response will be sent to the callBackURL set for the CollectionRequest
     * object or for the CollectionConfiguration . If it is POLLING, no response
     * will be sent to the callBackURL
     *
     * @param notificationType
     * @return
     */
    @Override
    public CollectionRequestWithCallBackUrl setNotificationType(final NotificationType notificationType) {
        this.notificationType = notificationType;
        return this;
    }

}
