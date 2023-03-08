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

        if (throwIfNullOrEmptyString(subscriptionKey)
                && throwIfNullOrEmptyString(referenceId)
                && throwIfNullOrEmptyString(apiKey)
                && throwIfNullObject(mode)
                && throwIfNullOrEmptyString(targetEnvironment)) {
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
    public static boolean throwIfNullOrEmptyString(final String stringValue) throws MoMoException {
        if (StringUtils.isNullOrEmpty(stringValue)) {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.INTERNAL_ERROR_CATEGORY,
                            Constants.GENERIC_ERROR_CODE)
                            .errorDescription(stringValue == null ? Constants.NULL_VALUE_ERROR : Constants.EMPTY_STRING_ERROR).build());
        } else {
            return true;
        }
    }

    /**
     * throws MoMoException with specified variable name if String is null or
     * empty otherwise returns true.
     *
     * @param stringValue
     * @param variableName
     * @return
     * @throws MoMoException
     */
    public static boolean throwIfNullOrEmptyString(final String stringValue, final String variableName) throws MoMoException {
        if (StringUtils.isNullOrEmpty(stringValue)) {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.INTERNAL_ERROR_CATEGORY,
                            Constants.GENERIC_ERROR_CODE)
                            .errorDescription(stringValue == null
                                    ? Constants.NULL_VALUE_ERROR_FOR_VARIABLE.replace(Constants.VARIABLE, variableName)
                                    : Constants.EMPTY_STRING_ERROR_FOR_VARIABLE.replace(Constants.VARIABLE, variableName)).build());
        } else {
            return true;
        }
    }

    /**
     * throws MoMoException if Object is null otherwise returns true.
     *
     * @param objectValue
     * @return
     * @throws MoMoException
     */
    public static boolean throwIfNullObject(final Object objectValue) throws MoMoException {
        if (Objects.isNull(objectValue)) {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.INTERNAL_ERROR_CATEGORY,
                            Constants.GENERIC_ERROR_CODE)
                            .errorDescription(Constants.NULL_VALUE_ERROR).build());
        } else {
            return true;
        }
    }

    /**
     * throws MoMoException with specified variable name if Object is null
     * otherwise returns true.
     *
     * @param objectValue
     * @param variableName
     * @return
     * @throws MoMoException
     */
    public static boolean throwIfNullObject(final Object objectValue, final String variableName) throws MoMoException {
        if (Objects.isNull(objectValue)) {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.INTERNAL_ERROR_CATEGORY,
                            Constants.GENERIC_ERROR_CODE)
                            .errorDescription(Constants.NULL_VALUE_ERROR_FOR_VARIABLE.replace(Constants.VARIABLE, variableName)).build());
        } else {
            return true;
        }
    }
}
