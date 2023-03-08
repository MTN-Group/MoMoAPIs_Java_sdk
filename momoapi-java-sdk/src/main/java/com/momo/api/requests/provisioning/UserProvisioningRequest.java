package com.momo.api.requests.provisioning;

import com.momo.api.base.constants.Constants;
import com.momo.api.base.context.provisioning.UserProvisioningContext;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.model.StatusResponse;
import com.momo.api.base.model.HttpErrorResponse;
import com.momo.api.base.util.StringUtils;
import com.momo.api.models.ApiKey;
import com.momo.api.models.ApiUser;
import com.momo.api.models.CallbackHost;

/**
 *
 * Class UserProvisioningRequest
 */
public class UserProvisioningRequest {

    /**
     * Used to create an API user in the sandbox target environment.
     *
     * @param callbackHost
     * @return
     * @throws MoMoException
     */
    public StatusResponse createUser(CallbackHost callbackHost) throws MoMoException {
        if (callbackHost == null) {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.VALIDATION_ERROR_CATEGORY,
                            Constants.VALUE_NOT_SUPPLIED_ERROR_CODE)
                            .errorDescription(Constants.CALL_BACK_HOST_OBJECT_INIT_ERROR).build());
        }
        return UserProvisioningContext.getContext().createUser(callbackHost);
    }

    /**
     * Used to get API user information.
     *
     * @param referenceId
     * @return
     * @throws MoMoException
     */
    public ApiUser getUserDetails(String referenceId) throws MoMoException {
        if (StringUtils.isNullOrEmpty(referenceId)) {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.INTERNAL_ERROR_CATEGORY,
                            Constants.GENERIC_ERROR_CODE).errorDescription(referenceId == null ? Constants.NULL_VALUE_ERROR : Constants.EMPTY_STRING_ERROR).build());
        }
        return UserProvisioningContext.getContext().getUserDetails(referenceId);
    }

    /**
     * Used to create an API key for an API user in the sandbox target
     * environment.
     *
     * @param referenceId
     * @return
     * @throws MoMoException
     */
    public ApiKey createApiKey(String referenceId) throws MoMoException {
        if (StringUtils.isNullOrEmpty(referenceId)) {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.INTERNAL_ERROR_CATEGORY,
                            Constants.GENERIC_ERROR_CODE).errorDescription(referenceId == null ? Constants.NULL_VALUE_ERROR : Constants.EMPTY_STRING_ERROR).build());
        }
        return UserProvisioningContext.getContext().createApiKey(referenceId);
    }
}
