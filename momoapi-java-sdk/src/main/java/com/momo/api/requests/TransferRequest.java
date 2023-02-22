package com.momo.api.requests;

import com.momo.api.base.constants.API;
import com.momo.api.base.constants.Constants;
import com.momo.api.base.constants.HttpMethod;
import com.momo.api.base.context.MoMoContext;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.model.StatusResponse;
import com.momo.api.base.model.HttpErrorResponse;
import com.momo.api.base.util.JSONFormatter;
import com.momo.api.base.util.StringUtils;
import com.momo.api.constants.RequestType;
import com.momo.api.models.Transfer;
import com.momo.api.models.TransferStatus;
import java.util.UUID;

/**
 *
 * Class TransferRequest
 */
public class TransferRequest extends CommonRequest {

    /**
     * 
     * @param subscriptionType
     * @param currentContext
     * @param transfer
     * @return
     * @throws MoMoException 
     */
    public StatusResponse transfer(String subscriptionType, MoMoContext currentContext, Transfer transfer) throws MoMoException {
        if (transfer == null) {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.VALIDATION_ERROR_CATEGORY,
                            Constants.VALUE_NOT_SUPPLIED_ERROR_CODE)
                            .errorDescription(Constants.TRANSFER_OBJECT_INIT_ERROR).build());
        }
        
        this.referenceId = UUID.randomUUID().toString();
        currentContext.getHTTPHeaders()
                .put(Constants.X_REFERENCE_ID, this.referenceId);

        String resourcePath = API.SUBSCRIPTION_VER_REQUEST
                .replace(Constants.SUBSCRIPTION_TYPE, subscriptionType)
                .replace(Constants.REQUEST_TYPE, RequestType.TRANSFER);
        StatusResponse statusResponse = createRequest(HttpMethod.POST, resourcePath, JSONFormatter.toJSON(transfer), notificationType, callBackURL, currentContext);
        return statusResponse;
    }
    
    /**
     * 
     * @param referenceId
     * @param subscriptionType
     * @param currentContext
     * @return
     * @throws MoMoException 
     */
    public TransferStatus getTransferStatus(String referenceId, String subscriptionType, MoMoContext currentContext) throws MoMoException {
        if (StringUtils.isNullOrEmpty(referenceId)) {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.INTERNAL_ERROR_CATEGORY,
                            Constants.GENERIC_ERROR_CODE).errorDescription(referenceId == null ? Constants.NULL_VALUE_ERROR : Constants.EMPTY_STRING_ERROR).build());
        }
        
        String resourcePath = API.SUBSCRIPTION_VER_REQUEST_REFERENCE_ID
                .replace(Constants.SUBSCRIPTION_TYPE, subscriptionType)
                .replace(Constants.REQUEST_TYPE, RequestType.TRANSFER)
                .replace(Constants.REFERENCE_ID, referenceId);
        return createRequest(HttpMethod.GET, resourcePath, null, notificationType, callBackURL, TransferStatus.class, currentContext);
    }
}
