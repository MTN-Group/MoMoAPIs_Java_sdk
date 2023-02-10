package com.momo.api.requests;

import com.momo.api.base.constants.API;
import com.momo.api.base.constants.Constants;
import com.momo.api.base.constants.HttpMethod;
import com.momo.api.base.context.MoMoContext;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.model.StatusResponse;
import com.momo.api.base.model.HttpErrorResponse;
import com.momo.api.base.util.ResourceUtil;
import com.momo.api.base.util.StringUtils;
import com.momo.api.constants.NotificationType;
import com.momo.api.constants.RequestType;
import com.momo.api.models.AccountBalance;
import com.momo.api.models.AccountHolder;
import com.momo.api.models.BasicUserInfo;
import com.momo.api.models.DeliveryNotification;
import com.momo.api.models.Result;

/**
 *
 * Class CommonRequest
 */
public class CommonRequest extends ResourceUtil {

    //TODO Make sure to remove methods that are not required for a usecase/module.
    protected NotificationType notificationType = NotificationType.CALLBACK;
    protected String callBackURL;
    
    /**
     * This operation is used to check if an account holder is registered and
     * active in the system.
     *
     * @param accountHolder
     * @param subscriptionType
     * @param currentContext
     * @return
     * @throws MoMoException
     */
    public Result validateAccountHolderStatus(AccountHolder accountHolder, String subscriptionType, MoMoContext currentContext) throws MoMoException {
        String resourcePath = getResourcePathAccountHolder(API.SUBSCRIPTION_VER_ACCOUNTHOLDER_STATUS, accountHolder);

        resourcePath = resourcePath
                .replace(Constants.SUBSCRIPTION_TYPE, subscriptionType);
        return createRequest(HttpMethod.GET, resourcePath, null, notificationType, callBackURL, Result.class, currentContext);
    }

    /**
     * Get the balance of the account.
     *
     * @param subscriptionType
     * @param currentContext
     * @return @throws MoMoException
     */
    public AccountBalance getAccountBalance(String subscriptionType, MoMoContext currentContext) throws MoMoException {
        String resourcePath = API.SUBSCRIPTION_VER_ACCOUNT_BALANCE
                .replace(Constants.SUBSCRIPTION_TYPE, subscriptionType);
        return createRequest(HttpMethod.GET, resourcePath, null, notificationType, callBackURL, AccountBalance.class, currentContext);
    }

    /**
     * This operation returns personal information of the account holder. The
     * operation does not need any consent by the account holder.
     *
     * @param msisdn
     * @param subscriptionType
     * @param currentContext
     * @return
     * @throws MoMoException
     */
    public BasicUserInfo getBasicUserinfo(String msisdn, String subscriptionType, MoMoContext currentContext) throws MoMoException {
        if (StringUtils.isNullOrEmpty(msisdn)) {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.INTERNAL_ERROR_CATEGORY,
                            Constants.GENERIC_ERROR_CODE).errorDescription(
                            msisdn == null ? Constants.NULL_VALUE_ERROR : Constants.EMPTY_STRING_ERROR).build()
            );
        }

        String resourcePath = API.SUBSCRIPTION_VER_ACCOUNTHOLDER_USER_INFO
                .replace(Constants.ACCOUNT_HOLDER_ID_TYPE, Constants.MSISDN)
                .replace(Constants.ACCOUNT_HOLDER_ID, msisdn);

        resourcePath = resourcePath
                .replace(Constants.SUBSCRIPTION_TYPE, subscriptionType);
        return createRequest(HttpMethod.GET, resourcePath, null, notificationType, callBackURL, BasicUserInfo.class, currentContext);
    }

    //TODO deliveryNotification is not mandatory for header. we can create separate method for accepting both if needed
    /**
     * This operation is used to send additional Notification to an End User.
     *
     * @param referenceId
     * @param deliveryNotification
     * @param deliveryNotificationHeader
     * @param haveHeader
     * @param subscriptionType
     * @param currentContext
     * @return
     * @throws MoMoException
     */
    public StatusResponse requestToPayDeliveryNotification(String referenceId, DeliveryNotification deliveryNotification, String deliveryNotificationHeader, boolean haveHeader, String subscriptionType, MoMoContext currentContext) throws MoMoException {

        if (StringUtils.isNullOrEmpty(referenceId)) {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.INTERNAL_ERROR_CATEGORY,
                            Constants.GENERIC_ERROR_CODE).errorDescription(referenceId == null ? Constants.NULL_VALUE_ERROR : Constants.EMPTY_STRING_ERROR).build());
        }

        if (deliveryNotification == null) {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.VALIDATION_ERROR_CATEGORY,
                            Constants.VALUE_NOT_SUPPLIED_ERROR_CODE)
                            .errorDescription(Constants.DELIVERY_NOTIFICATION_OBJECT_INIT_ERROR).build());
        }

        if (haveHeader && deliveryNotificationHeader == null) {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.INTERNAL_ERROR_CATEGORY,
                            Constants.GENERIC_ERROR_CODE).errorDescription(Constants.NULL_VALUE_ERROR).build());
        } else if (haveHeader) {
            currentContext.getHTTPHeaders()
                    .put(Constants.NOTIFICATION_MESSAGE, deliveryNotificationHeader);
        }

        String resourcePath = API.SUBSCRIPTION_VER_REQUEST_REFERENCE_ID_DELIVERYNOTIFICATION
                .replace(Constants.SUBSCRIPTION_TYPE, subscriptionType)
                .replace(Constants.REQUEST_TYPE, RequestType.REQUEST_TO_PAY)
                .replace(Constants.REFERENCE_ID, referenceId);
        StatusResponse statusResponse = createRequest(HttpMethod.POST, resourcePath, deliveryNotification.toJSON(), notificationType, callBackURL, currentContext);
        if (haveHeader) {
            currentContext.getHTTPHeaders()
                    .remove(Constants.NOTIFICATION_MESSAGE);
        }
        return statusResponse;
    }

    /**
     * This callBackURL will have higher priority and will override the
     * callBackURL set for the Context
     *
     * @param callBackURL
     * @return
     */
    public CommonRequest addCallBackUrl(final String callBackURL) {
        this.callBackURL = callBackURL;
        return setNotificationType(NotificationType.CALLBACK);
    }

    /**
     * NotificationType can be set to CALLBACK or POLLING. If it is CALLBACK, a
     * response will be sent to the callBackURL set for the current context of
     * the request. If it is POLLING, no response will be sent to the
     * callBackURL
     *
     * @param notificationType
     * @return
     */
    public CommonRequest setNotificationType(final NotificationType notificationType) {
        this.notificationType = notificationType;
        return this;
    }
}
