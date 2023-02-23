package com.momo.api.requests;

import com.momo.api.base.constants.API;
import com.momo.api.base.constants.AccessType;
import com.momo.api.base.constants.Constants;
import com.momo.api.base.constants.HttpMethod;
import com.momo.api.base.constants.IdType;
import com.momo.api.base.context.MoMoContext;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.model.BCAuthorize;
import com.momo.api.base.model.StatusResponse;
import com.momo.api.base.model.HttpErrorResponse;
import com.momo.api.base.util.ResourceUtil;
import com.momo.api.base.util.StringUtils;
import com.momo.api.base.util.Validator;
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

    protected String referenceId;

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
    protected Result validateAccountHolderStatus(AccountHolder accountHolder, String subscriptionType, MoMoContext currentContext) throws MoMoException {
        String resourcePath = getResourcePathAccountHolder(API.SUBSCRIPTION_VER_ACCOUNTHOLDER_STATUS, accountHolder);

        resourcePath = resourcePath
                .replace(Constants.SUBSCRIPTION_TYPE, subscriptionType);
        return createRequest(HttpMethod.GET, resourcePath, null, NotificationType.POLLING, null, Result.class, currentContext);
    }

    /**
     * Get the balance of the account.
     *
     * @param subscriptionType
     * @param currentContext
     * @return @throws MoMoException
     */
    protected AccountBalance getAccountBalance(String subscriptionType, MoMoContext currentContext) throws MoMoException {
        String resourcePath = API.SUBSCRIPTION_VER_ACCOUNT_BALANCE
                .replace(Constants.SUBSCRIPTION_TYPE, subscriptionType);
        return createRequest(HttpMethod.GET, resourcePath, null, NotificationType.POLLING, null, AccountBalance.class, currentContext);
    }

    /**
     * This operation returns personal information of the account holder.The
 operation does not need any consent by the account holder.
     *
     * @param msisdn
     * @param subscriptionType
     * @param currentContext
     * @return
     * @throws MoMoException
     */
    protected BasicUserInfo getBasicUserinfo(String msisdn, String subscriptionType, MoMoContext currentContext) throws MoMoException {
        if (StringUtils.isNullOrEmpty(msisdn)) {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.INTERNAL_ERROR_CATEGORY,
                            Constants.GENERIC_ERROR_CODE).errorDescription(
                            msisdn == null ? Constants.NULL_VALUE_ERROR : Constants.EMPTY_STRING_ERROR).build()
            );
        }

        String resourcePath = API.SUBSCRIPTION_VER_ACCOUNTHOLDER_USER_INFO
                .replace(Constants.ACCOUNT_HOLDER_ID_TYPE, IdType.msisdn.getValue())
                .replace(Constants.ACCOUNT_HOLDER_ID, msisdn);

        resourcePath = resourcePath
                .replace(Constants.SUBSCRIPTION_TYPE, subscriptionType);
        return createRequest(HttpMethod.GET, resourcePath, null, NotificationType.POLLING, null, BasicUserInfo.class, currentContext);
    }

    /**
     * This operation is used to send additional Notification to an End User.
     *
     * @param referenceId
     * @param deliveryNotification
     * @param deliveryNotificationHeader
     * @param language
     * @param subscriptionType
     * @param currentContext
     * @return
     * @throws MoMoException
     */
    protected StatusResponse requestToPayDeliveryNotification(String referenceId, DeliveryNotification deliveryNotification, String deliveryNotificationHeader, String language, String subscriptionType, MoMoContext currentContext) throws MoMoException {

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
        
        if(!StringUtils.isNullOrEmpty(deliveryNotificationHeader)){
            currentContext.getHTTPHeaders()
                    .put(Constants.NOTIFICATION_MESSAGE, deliveryNotificationHeader);
        }
        
        //TODO may need to validate with "An ISO 639-1 or ISO 639-3 language code."
        if(!StringUtils.isNullOrEmpty(language)){
            currentContext.getHTTPHeaders()
                    .put(Constants.LANGUAGE, language);
        }

        String resourcePath = API.SUBSCRIPTION_VER_REQUEST_REFERENCE_ID_DELIVERYNOTIFICATION
                .replace(Constants.SUBSCRIPTION_TYPE, subscriptionType)
                .replace(Constants.REQUEST_TYPE, RequestType.REQUEST_TO_PAY)
                .replace(Constants.REFERENCE_ID, referenceId);
        StatusResponse statusResponse = createRequest(HttpMethod.POST, resourcePath, deliveryNotification.toJSON(), NotificationType.POLLING, null, currentContext);
        return statusResponse;
    }

    /**
     * This operation is used to claim a consent by the account holder for the
     * requested scopes.bcAuthorize receives a parameter "auth_req_id" which is
     * passed into Oauth2 API which is then used in getUserInfoWithConsent API
     *
     * @param accountHolder
     * @param scope
     * @param accesType
     * @param subscriptionType
     * @param currentContext
     * @param notificationType
     * @param callBackURL
     * @return
     * @throws MoMoException
     */
    protected BCAuthorize bcAuthorize(AccountHolder accountHolder, String scope, AccessType accesType, String subscriptionType, MoMoContext currentContext, NotificationType notificationType, String callBackURL) throws MoMoException {
        Validator.throwIfNullObject(accountHolder);
        Validator.throwIfNullOrEmptyString(accountHolder.getAccountHolderId());
        Validator.throwIfNullOrEmptyString(accountHolder.getAccountHolderIdType());
        Validator.throwIfNullOrEmptyString(scope);
        Validator.throwIfNullObject(accesType);

        currentContext.getHTTPHeaders()
                .put(Constants.HTTP_CONTENT_TYPE_HEADER, Constants.HTTP_CONTENT_TYPE_URLENCODED);

        String resourcePath = API.SUBSCRIPTION_VER_REQUEST
                .replace(Constants.SUBSCRIPTION_TYPE, subscriptionType)
                .replace(Constants.REQUEST_TYPE, RequestType.BC_AUTHORIZE);

        //TODO possible values for "msisdn", "scope" and "access_type" 
        //TODO make sure no unwanted strings are passed in as parameters eg:- "/"
        String payLoad = "login_hint=ID:" + accountHolder.getAccountHolderId() + "/" + accountHolder.getAccountHolderIdType() + "&scope=" + scope + "&access_type=" + accesType.getValue();

        BCAuthorize bcAuthorize = createRequest(HttpMethod.POST, resourcePath, payLoad, notificationType, callBackURL, BCAuthorize.class, currentContext);

        return bcAuthorize;
    }
}
