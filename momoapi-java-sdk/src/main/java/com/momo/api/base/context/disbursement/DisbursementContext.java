package com.momo.api.base.context.disbursement;

import com.momo.api.base.constants.Constants;
import com.momo.api.base.constants.SubscriptionType;
import com.momo.api.base.context.MoMoAuthentication;
import com.momo.api.base.context.MoMoContext;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.model.AccessToken;
import com.momo.api.base.model.HttpErrorResponse;
import com.momo.api.constants.Environment;
import java.util.Map;

/**
 *
 * Class DisbursementContext
 */
public class DisbursementContext implements MoMoContext {

    // Singleton instance
    private static DisbursementContext instance;

    // Set global callback url
    private String callBackUrl;

    private MoMoAuthentication credential;
    private AccessToken accessToken;

    private DisbursementContext(String subscriptionKey, String referenceId, String apiKey, Environment mode, String callBackUrl, Map<String, String> configurations, String targetEnvironment) throws MoMoException {
        this.credential = new MoMoAuthentication(subscriptionKey, SubscriptionType.DISBURSEMENT, referenceId, apiKey);
        if (configurations != null && configurations.size() > 0) {
            this.credential.addConfigurations(configurations);
        }

        this.setMode(mode);
        this.setTargetEnvironment(targetEnvironment);
        this.callBackUrl = callBackUrl;

        this.accessToken = this.credential.createAccessToken();
        instance = this;
    }

    public static void createContext(String subscriptionKey, String referenceId, String apiKey, Environment mode, String callBackUrl, Map<String, String> configurations, String targetEnvironment) throws MoMoException {
        if (instance == null) {
            synchronized (DisbursementContext.class) {
                if (instance == null) {
                    instance = new DisbursementContext(subscriptionKey, referenceId, apiKey, mode, callBackUrl, configurations, targetEnvironment);
                }
            }
        }
        instance.callBackUrl = callBackUrl;
    }

    /**
     * *
     * Returns context instance
     *
     * @return
     * @throws MoMoException
     */
    public static DisbursementContext getContext() throws MoMoException {
        if (instance == null) {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.INTERNAL_ERROR_CATEGORY,
                            Constants.GENERIC_ERROR_CODE).errorDescription(Constants.REQUEST_NOT_CREATED).build());
        } else {
            return instance;
        }
    }

    /**
     * *
     * Returns context status
     *
     * @return
     */
    public static boolean contextExists() {
        return instance != null;
    }

    /**
     * *
     * Returns access token
     *
     * @return
     * @throws MoMoException
     */
    @Override
    public AccessToken fetchAccessToken() throws MoMoException {
        if (this.credential != null) {
            this.accessToken =  this.credential.createAccessToken();
            return this.accessToken;
        }//TODO may need an else case to handle if "this.credential" is null
        return null;
    }

    /**
     * *
     * Returns refresh token
     *
     * @return
     * @throws MoMoException
     */
    @Override
    public AccessToken getRefreshToken() throws MoMoException {
        if (this.credential != null) {
            this.accessToken =  this.credential.getRefreshToken();
            return this.accessToken;
        }//TODO may need an else case to handle if "this.credential" is null
        return null;
    }

    /**
     * *
     * Returns configuration map
     *
     * @return
     */
    @Override
    public Map<String, String> getConfigurationMap() {
        return this.credential.getConfigurations();
    }

    /**
     * *
     * Returns HTTP header map
     *
     * @return
     */
    @Override
    public Map<String, String> getHTTPHeaders() {
        return this.credential.getHeaders();
    }

    /**
     * *
     * Returns HTTP Header value
     *
     * @param key
     * @return
     */
    @Override
    public String getHTTPHeader(String key) {
        return this.credential.getHeader(key);
    }

    /**
     * *
     *
     * @return
     */
    @Override
    public String getCallBackUrl() {
        return callBackUrl;
    }

    /**
     * *
     * Add HTTP Header value to existing list of HTTP Headers
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public DisbursementContext addHTTPHeader(String key, String value) {
        this.credential.addHeader(key, value);
        return this;
    }

    /**
     * *
     * Sets mode to either `production` or `sandbox`
     *
     * @param mode
     * @return
     */
    @Override
    public DisbursementContext setMode(Environment mode) {
        if (mode == null || !(mode.equals(Environment.PRODUCTION) || mode.equals(Environment.SANDBOX))) {
            throw new IllegalArgumentException(String.format("Mode needs to be either `%s` or `%s`.", Environment.SANDBOX, Environment.PRODUCTION));
        }
        this.credential.addConfiguration(Constants.MODE, mode.name());
        return this;
    }

    /**
     * Set the TargetEnvironment based on the parameter passed in
     *
     * @param targetEnvironment
     * @return
     */
    @Override
    public MoMoContext setTargetEnvironment(String targetEnvironment) {
        this.credential.addConfiguration(Constants.TARGET_ENVIRONMENT, targetEnvironment);
        this.addHTTPHeader(Constants.TARGET_ENVIRONMENT, targetEnvironment);
        return this;
    }

    /**
     * This will destroy the singleton object of DisbursementContext and
     * requests will no longer work
     */
    public static void destroySingletonObject() {
        DisbursementContext.instance = null;
    }
}
