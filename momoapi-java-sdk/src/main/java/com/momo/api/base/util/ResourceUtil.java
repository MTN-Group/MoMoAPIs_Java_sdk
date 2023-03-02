package com.momo.api.base.util;

import com.momo.api.base.APIManager;
import com.momo.api.base.ConfigManager;
import com.momo.api.base.ConnectionManager;
import com.momo.api.base.ExecuteTask;
import com.momo.api.base.HttpConfiguration;
import com.momo.api.base.HttpConnection;
import com.momo.api.base.HttpResponse;
import com.momo.api.base.HttpStatusCode;
import com.momo.api.base.SDKUtil;
import com.momo.api.base.constants.Constants;
import com.momo.api.base.constants.HttpMethod;
import com.momo.api.base.context.MoMoContext;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.exception.UnauthorizedException;
import com.momo.api.base.model.AccessToken;
import com.momo.api.base.model.StatusResponse;
import com.momo.api.base.model.HttpErrorResponse;
import com.momo.api.constants.NotificationType;
import com.momo.api.models.AccountHolder;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Class ResourceUtil
 */
public class ResourceUtil {

    // Used for dynamic configuration
    private static Map<String, String> configurationMap;

    /**
     * *
     * Process requests
     *
     * @param httpMethod
     * @param resourcePath
     * @param responseObject
     * @param <T>
     * @param currentContext
     * @return
     * @throws MoMoException
     */
    protected <T> T createRequest(HttpMethod httpMethod, String resourcePath, Class<T> responseObject, MoMoContext currentContext)
            throws MoMoException {
        return createRequest(httpMethod, resourcePath, null, null, null, responseObject, currentContext);
    }

    /**
     * *
     * Process requests
     *
     * @param httpMethod
     * @param resourcePath
     * @param payLoad
     * @param notificationType
     * @param callBackURL
     * @param responseObject
     * @param <T>
     * @param currentContext
     * @return
     * @throws MoMoException
     */
    protected <T> T createRequest(HttpMethod httpMethod, String resourcePath, String payLoad,
            NotificationType notificationType, String callBackURL, Class<T> responseObject, MoMoContext currentContext)
            throws MoMoException {
        T sdkResponse = null;

        HttpResponse requestResponse = requestExecute(httpMethod, resourcePath, payLoad, notificationType, callBackURL, currentContext, null);

        if (requestResponse.getPayLoad() instanceof String) {
            sdkResponse = JSONFormatter.fromJSON((String) requestResponse.getPayLoad(), responseObject);
        }

        return sdkResponse;
    }

    /**
     * *
     * Process requests
     *
     * @param httpMethod
     * @param resourcePath
     * @param payLoad
     * @param notificationType
     * @param callBackURL
     * @param responseObject
     * @param <T>
     * @param currentContext
     * @param auth_req_id
     * @return
     * @throws MoMoException
     */
    protected <T> T createRequest(HttpMethod httpMethod, String resourcePath, String payLoad,
            NotificationType notificationType, String callBackURL, Class<T> responseObject, MoMoContext currentContext, String auth_req_id)
            throws MoMoException {
        T sdkResponse = null;
        HttpResponse requestResponse = requestExecute(httpMethod, resourcePath, payLoad, notificationType, callBackURL, currentContext, auth_req_id);

        if (requestResponse.getPayLoad() instanceof String) {
            sdkResponse = JSONFormatter.fromJSON((String) requestResponse.getPayLoad(), responseObject);
        }

        return sdkResponse;
    }

    /**
     * Returns StatusResponse from here, since there is no proper response from
     * the API
     *
     * @param httpMethod
     * @param resourcePath
     * @param payLoad
     * @param notificationType
     * @param callBackURL
     * @param currentContext
     * @return
     * @throws MoMoException
     */
    protected StatusResponse createRequest(HttpMethod httpMethod, String resourcePath, String payLoad,
            NotificationType notificationType, String callBackURL, MoMoContext currentContext)
            throws MoMoException {
        HttpResponse requestResponse = requestExecute(httpMethod, resourcePath, payLoad, notificationType, callBackURL, currentContext, null);
        StatusResponse statusResponse = new StatusResponse();

        statusResponse.setStatus(requestResponse.isSuccess());
        if (requestResponse.getReferenceId() != null) {
            statusResponse.setReferenceId(requestResponse.getReferenceId());
        }
        return statusResponse;
    }

    /**
     * *
     * Configure requests first and then execute
     *
     * @param httpMethod
     * @param resourcePath
     * @param currentContext
     * @return
     * @throws MoMoException
     */
    protected static HttpResponse requestExecute(HttpMethod httpMethod, String resourcePath, MoMoContext currentContext)
            throws MoMoException {
        return requestExecute(httpMethod, resourcePath, null, NotificationType.CALLBACK, null, currentContext, null);
    }

    /**
     * *
     * Configure requests first and then execute - with payload
     *
     * @param httpMethod
     * @param resourcePath
     * @param payLoad
     * @param currentContext
     * @return
     * @throws MoMoException
     */
    protected static HttpResponse requestExecute(HttpMethod httpMethod, String resourcePath, String payLoad, MoMoContext currentContext)
            throws MoMoException {
        return requestExecute(httpMethod, resourcePath, payLoad, NotificationType.CALLBACK, null, currentContext, null);
    }

    /**
     * *
     * Configure requests first and then execute - with payload and callBack
     *
     * @param httpMethod
     * @param resourcePath
     * @param payLoad
     * @param notificationType
     * @param callBackURL
     * @param currentContext
     * @param auth_req_id
     * @return
     * @throws MoMoException
     */
    protected static HttpResponse requestExecute(HttpMethod httpMethod, String resourcePath, String payLoad,
            NotificationType notificationType, String callBackURL, MoMoContext currentContext, String auth_req_id) throws MoMoException {
        HttpResponse responseData = null;
        Map<String, String> cMap;
        Map<String, String> headersMap;

        if (currentContext != null) {
            AccessToken accessToken = (!StringUtils.isNullOrEmpty(auth_req_id)) ? currentContext.fetchOauth2Token(auth_req_id) : currentContext.fetchAccessToken();
            if (accessToken == null || accessToken.getAccess_token() == null) {
                throw new IllegalArgumentException(Constants.EMPTY_ACCESS_TOKEN_MESSAGE);
            }

            if (!currentContext.getHTTPHeaders().containsKey(Constants.HTTP_CONTENT_TYPE_HEADER) || currentContext.getHTTPHeader(Constants.HTTP_CONTENT_TYPE_HEADER) == null) {
                currentContext.addHTTPHeader(Constants.HTTP_CONTENT_TYPE_HEADER, Constants.HTTP_CONTENT_TYPE_JSON);
            }

            if (currentContext.getHTTPHeader(Constants.TARGET_ENVIRONMENT) == null) {
                if (currentContext.getConfigurationMap().containsKey(Constants.TARGET_ENVIRONMENT) && !StringUtils.isNullOrEmpty(currentContext.getConfigurationMap().get(Constants.TARGET_ENVIRONMENT))) {
                    currentContext.addHTTPHeader(Constants.TARGET_ENVIRONMENT, currentContext.getConfigurationMap().get(Constants.TARGET_ENVIRONMENT));
                } else {
                    currentContext.addHTTPHeader(Constants.TARGET_ENVIRONMENT, currentContext.getConfigurationMap().get("mode").toLowerCase());
                }
            }

            // Set call back URL
            //TODO do we need to check if callBackURL contains a subsrting of the CallbackHost? If so, is it possible in production to get the CallbackHost?
            if (NotificationType.POLLING != notificationType && (!StringUtils.isNullOrEmpty(callBackURL)
                    || !StringUtils.isNullOrEmpty(currentContext.getCallBackUrl()))) {
                if (!StringUtils.isNullOrEmpty(callBackURL)) {
                    if (isValidURL(callBackURL)) {
                        //callBack Url set with the value passed in for current request
                        currentContext.addHTTPHeader(Constants.CALL_BACK_URL, callBackURL);
                    } else {
                        throw new MoMoException(
                                new HttpErrorResponse.HttpErrorResponseBuilder(Constants.VALIDATION_ERROR_CATEGORY,
                                        Constants.INVALID_FORMAT_CODE).errorDescription(Constants.INVALID_CALLBACK_URL_FORMAT_ERROR).build());
                    }
                } else if (!StringUtils.isNullOrEmpty(currentContext.getCallBackUrl())) {
                    if (isValidURL(currentContext.getCallBackUrl())) {
                        //callBack Url set with the value passed during the creation of request/context. This will be the default one url if not specified for each request.
                        currentContext.addHTTPHeader(Constants.CALL_BACK_URL, currentContext.getCallBackUrl());
                    } else {
                        throw new MoMoException(
                                new HttpErrorResponse.HttpErrorResponseBuilder(Constants.VALIDATION_ERROR_CATEGORY,
                                        Constants.INVALID_FORMAT_CODE).errorDescription(Constants.INVALID_CALLBACK_URL_FORMAT_ERROR).build());
                    }
                }
            } else {
                if (currentContext.getHTTPHeaders().containsKey(Constants.CALL_BACK_URL)) {
                    currentContext.getHTTPHeaders().remove(Constants.CALL_BACK_URL);
                }
            }

            if (currentContext.getConfigurationMap() != null) {
                cMap = SDKUtil.combineDefaultMap(currentContext.getConfigurationMap());
            } else {
                initializeToDefault();

                // Merge with existing configuration map
                cMap = new HashMap<>(configurationMap);
            }

            headersMap = currentContext.getHTTPHeaders();
            headersMap.put(Constants.AUTHORIZATION_HEADER, Constants.BEARER + accessToken.getAccess_token());

            APIManager apiManager = new APIManager(cMap, headersMap);

            // Need to change
            apiManager.setResourcePoint(resourcePath);

            if (payLoad != null) {
                apiManager.setPayLoad(payLoad);
            }

            HttpConfiguration httpConfiguration = getHttpConfiguration(httpMethod, apiManager);

            responseData = executeWithRetries(currentContext, auth_req_id, () -> execute(apiManager, httpConfiguration));

            validateResponseData(responseData);
        } else {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.INTERNAL_ERROR_CATEGORY,
                            Constants.GENERIC_ERROR_CODE).errorDescription(Constants.REQUEST_NOT_CREATED).build());
        }

        return responseData;
    }

    /**
     * *
     * Initialize to default properties
     */
    protected static void initializeToDefault() {
        configurationMap = SDKUtil.combineDefaultMap(ConfigManager.getInstance().getConfigurationMap());
    }

    /**
     * Returns resource path, which is created using AccountHolder
     *
     * @param requestEndPoint
     * @param accountHolder
     * @return
     * @throws MoMoException
     */
    protected static String getResourcePathAccountHolder(final String requestEndPoint, AccountHolder accountHolder)
            throws MoMoException {
        if (accountHolder == null) {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.VALIDATION_ERROR_CATEGORY,
                            Constants.VALUE_NOT_SUPPLIED_ERROR_CODE)
                            .errorDescription(Constants.ACCOUNT_HOLDER_OBJECT_INIT_ERROR).build());
        }
        if (accountHolder.getAccountHolderIdType() == null || accountHolder.getAccountHolderId() == null) {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.INTERNAL_ERROR_CATEGORY,
                            Constants.GENERIC_ERROR_CODE).errorDescription(Constants.NULL_VALUE_ERROR).build());
        }
        if (accountHolder.getAccountHolderIdType().isEmpty() || accountHolder.getAccountHolderId().isEmpty()) {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.INTERNAL_ERROR_CATEGORY,
                            Constants.GENERIC_ERROR_CODE).errorDescription(Constants.EMPTY_STRING_ERROR).build());
        }

        String resourcePath = requestEndPoint
                .replace(Constants.ACCOUNT_HOLDER_ID_TYPE, accountHolder.getAccountHolderIdType())
                .replace(Constants.ACCOUNT_HOLDER_ID, accountHolder.getAccountHolderId());

        if (StringUtils.isNullOrEmpty(resourcePath)) {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.INTERNAL_ERROR_CATEGORY,
                            Constants.GENERIC_ERROR_CODE).errorDescription(resourcePath == null ? Constants.NULL_VALUE_ERROR : Constants.EMPTY_STRING_ERROR).build());
        }

        return resourcePath;
    }

    /**
     * *
     * Configure HTTP headers for API request
     *
     * @param httpMethod
     * @param apiManager
     * @return
     */
    private static HttpConfiguration getHttpConfiguration(HttpMethod httpMethod, APIManager apiManager)
            throws MoMoException {
        String endpoint = apiManager.getResourceEndPoint();

        if (StringUtils.isNullOrEmpty(endpoint)) {
            throw new MoMoException(Constants.INVALID_REST_ENDPOINT);
        }

        HttpConfiguration httpConfiguration = new HttpConfiguration();
        httpConfiguration.setHttpMethod(httpMethod.toString());
        httpConfiguration.setEndPointUrl(endpoint);

        return httpConfiguration;
    }

    /**
     * *
     * Executes request trigger, re-trigger same call if token expired
     *
     * @param apiContext
     * @param task
     * @return
     */
    private static HttpResponse executeWithRetries(MoMoContext currentContext, String auth_req_id, ExecuteTask task)
            throws MoMoException {
        int count = 0;
        while (count < Constants.MAX_RETRIES) {
            try {
                return task.execute();
            } catch (UnauthorizedException e) {
                System.out.println("1---------------------------------------------------token expired:");
                AccessToken accessToken = (!StringUtils.isNullOrEmpty(auth_req_id)) ? currentContext.getRefreshOauth2Token(auth_req_id) : currentContext.getRefreshToken();
                if (accessToken == null || accessToken.getAccess_token() == null) {
                    System.out.println("2---------------------------------------------------token expired error:");
                    throw new IllegalArgumentException(Constants.EMPTY_ACCESS_TOKEN_MESSAGE);
                }
                currentContext.getHTTPHeaders().put(Constants.AUTHORIZATION_HEADER, Constants.BEARER + accessToken.getAccess_token());

                if (++count >= Constants.MAX_RETRIES) {
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * *
     * Execute the REST API call and return response
     *
     * @param apiManager
     * @param httpConfiguration
     * @return
     */
    private static HttpResponse execute(APIManager apiManager, HttpConfiguration httpConfiguration)
            throws MoMoException, UnauthorizedException {
        HttpResponse responseData;
        HttpConnection httpConnection;

        try {
            httpConnection = ConnectionManager.getInstance().getConnection();
            httpConnection.createAndConfigureHttpConnection(httpConfiguration);

            responseData = httpConnection.execute(httpConfiguration.getEndPointUrl(), apiManager.getPayLoad(),
                    apiManager.getHeaderMap());

            // Token expired
            if (responseData.getResponseCode() == HttpStatusCode.UNAUTHORIZED) {
                throw new UnauthorizedException();
            }
        } catch (IOException e) {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.INTERNAL_ERROR_CATEGORY,
                            Constants.GENERIC_ERROR_CODE).errorDescription(Constants.GENRAL_ERROR).build());
        }

        return responseData;
    }

    /**
     * *
     * Get count based on the key from headerMap
     *
     * @param headerMap
     * @param key
     * @return
     */
    public static Integer getRecordsCount(Map<String, List<String>> headerMap, String key) {
        List<String> headerKey = (List<String>) headerMap.get(key);

        String countString = headerMap.containsKey(key)
                ? (headerKey.size() > 0
                ? headerKey.get(0)
                : null)
                : null;
        try {
            return Integer.parseInt(countString);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * *
     * Check if URL is valid
     *
     * @param URL
     * @return
     */
    public static boolean isValidURL(String URL) {
        return URL.matches("^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
    }

    /**
     * Check if the HttpResponse received is an error
     *
     * @param responseData
     * @throws MoMoException
     */
    public static void validateResponseData(HttpResponse responseData) throws MoMoException {
        if (responseData == null) {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.INTERNAL_ERROR_CATEGORY,
                            Constants.GENERIC_ERROR_CODE).errorDescription(Constants.GENRAL_ERROR).build());
        } else if (responseData.getPayLoad() == null) {
            HttpStatusCode httpStatusCode = responseData.getResponseCode();
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorReasonBuilder(
                            Integer.toString(httpStatusCode.getHttpStatusCode()),
                            httpStatusCode.getHttpCode()).build());
        } else if (!responseData.isSuccess() && responseData.getPayLoad() instanceof String) {
            HttpStatusCode httpStatusCode = responseData.getResponseCode();
            HttpErrorResponse errorResponse = JSONFormatter.fromJSON((String) responseData.getPayLoad(),
                    HttpErrorResponse.class);
            if (errorResponse.getCode() != null && errorResponse.getMessage() != null) {
                throw new MoMoException(
                        new HttpErrorResponse.HttpErrorReasonBuilder(
                                Integer.toString(
                                        httpStatusCode.getHttpStatusCode()),
                                //                                null,
                                errorResponse.getCode(),
                                errorResponse.getMessage()).build());
            } else if (errorResponse.getError() != null) {
                throw new MoMoException(
                        new HttpErrorResponse.HttpErrorBuilder(
                                Integer.toString(
                                        httpStatusCode.getHttpStatusCode()),
                                //                                null,
                                errorResponse.getError()).build());
            } else {
                throw new MoMoException(errorResponse);
            }
        }
    }
}
