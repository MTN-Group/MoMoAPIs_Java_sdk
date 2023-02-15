package com.momo.api.base.util;

import com.momo.api.base.constants.Constants;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.model.HttpErrorResponse;
import com.momo.api.constants.Environment;
import java.util.Objects;

/**
 *
 * Class Validator
 */
public class Validator {

    /**
     * returns true if all values are valid.
     *
     * @param subscriptionKey
     * @param referenceId
     * @param apiKey
     * @param mode
     * @param targetEnvironment
     * @return
     * @throws MoMoException
     */
    public static boolean configurationValidator(String subscriptionKey, String referenceId, String apiKey, Environment mode, String targetEnvironment) throws MoMoException {

        if (throwIfNullOrEmpty(subscriptionKey)
                && throwIfNullOrEmpty(referenceId)
                && throwIfNullOrEmpty(apiKey)
                && !Objects.isNull(mode)
                && throwIfNullOrEmpty(targetEnvironment)) {
            return true;
        } else {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.INTERNAL_ERROR_CATEGORY,
                            Constants.GENERIC_ERROR_CODE)
                            .errorDescription(Constants.NULL_ENVIRONMENT_ERROR).build());
        }

    }

    /**
     * throws MoMoException if String is null or empty otherwise returns true.
     *
     * @param stringValue
     * @return
     * @throws MoMoException
     */
    public static boolean throwIfNullOrEmpty(final String stringValue) throws MoMoException {
        if (StringUtils.isNullOrEmpty(stringValue)) {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.INTERNAL_ERROR_CATEGORY,
                            Constants.GENERIC_ERROR_CODE)
                            .errorDescription(stringValue == null ? Constants.NULL_VALUE_ERROR : Constants.EMPTY_STRING_ERROR).build());
        } else {
            return true;
        }
    }

    //TODO we can use this method if required for all validations. not yet used
    /**
     * throws MoMoException with specified variable name if String is null or
     * empty otherwise returns true.
     *
     * @param stringValue
     * @param variableName
     * @return
     * @throws MoMoException
     */
    public static boolean throwIfNullOrEmpty(final String stringValue, final String variableName) throws MoMoException {
        if (StringUtils.isNullOrEmpty(stringValue)) {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.INTERNAL_ERROR_CATEGORY,
                            Constants.GENERIC_ERROR_CODE)
                            .errorDescription(stringValue == null
                                    ? Constants.NULL_VALUE_ERROR + " for " + variableName
                                    : Constants.EMPTY_STRING_ERROR + " for " + variableName).build());
        } else {
            return true;
        }
    }
}
