package com.momo.api.base.context.provisioning;

import com.momo.api.base.context.UserProvisioningAuthentication;
import com.momo.api.base.constants.Constants;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.model.StatusResponse;
import com.momo.api.base.model.HttpErrorResponse;
import com.momo.api.constants.Environment;
import com.momo.api.models.ApiKey;
import com.momo.api.models.ApiUser;
import com.momo.api.models.CallbackHost;
import java.util.Map;

public class UserProvisioningContext {

    // Singleton instance
    private static UserProvisioningContext instance;

    private UserProvisioningAuthentication userProvisioningAuthentication;
    private String subscriptionKey;
    private ApiKey apiKey;
    private ApiUser apiUser;

    /**
     * private constructor for UserProvisioningContext. The context gets
     * initialized here with the parameters provided.
     *
     * @param subscriptionKey
     * @param mode
     * @param providerCallbackHostApiUser
     * @param configurations
     */
    private UserProvisioningContext(String subscriptionKey, Environment mode, Map<String, String> configurations) {
        this.subscriptionKey = subscriptionKey;
        this.userProvisioningAuthentication = new UserProvisioningAuthentication(this.subscriptionKey);
        if (configurations != null && configurations.size() > 0) {
            this.userProvisioningAuthentication.addConfigurations(configurations);
        }
        this.setMode(mode);
    }

    /**
     * A new API User will be created.The ReferenceId(ApiUser/UUID) will be returned
     * inside StatusResponse object after creation.
     *
     * @param callbackHost
     * @return
     * @throws MoMoException
     */
    public StatusResponse createUser(CallbackHost callbackHost) throws MoMoException {
        StatusResponse statusResponse = this.userProvisioningAuthentication.createUser(callbackHost);
        instance = this;
        return statusResponse;
    }

    /**
     * Returns details of the API User having the provided referenceId(ApiUser/UUID)
     *
     * @param referenceId
     * @return
     * @throws MoMoException
     */
    public ApiUser getUserDetails(String referenceId) throws MoMoException {
        this.apiUser = this.userProvisioningAuthentication.getUserDetails(referenceId);
        instance = this;
        return this.apiUser;
    }

    /**
     * Generates ApiKey for the provided referenceId(ApiUser/UUID)
     *
     * @param referenceId
     * @return
     * @throws MoMoException
     */
    public ApiKey createApiKey(String referenceId) throws MoMoException {
        this.apiKey = this.userProvisioningAuthentication.createApiKey(referenceId);
        instance = this;
        return this.apiKey;
    }

    /**
     * For creating and maintaining a single instance of the class
     *
     * @param subscriptionKey
     * @param mode
     * @param configurations
     * @return
     */
    public static UserProvisioningContext createContext(String subscriptionKey, Environment mode, Map<String, String> configurations) {
        if (instance == null) {
            synchronized (UserProvisioningContext.class) {
                if (instance == null) {
                    instance = new UserProvisioningContext(subscriptionKey, mode, configurations);
                }
            }
        }
        return instance;
    }

    /**
     * *
     * Returns context instance
     *
     * @return
     * @throws com.momo.api.base.exception.MoMoException
     */
    public static UserProvisioningContext getContext() throws MoMoException {
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
    public static boolean conextExists() {
        return instance != null;
    }

    /**
     * *
     * Returns configuration map
     *
     * @return
     */
    public Map<String, String> getConfigurationMap() {
        return this.userProvisioningAuthentication.getConfigurations();
    }

    /**
     * *
     * Returns HTTP header map
     *
     * @return
     */
    public Map<String, String> getHTTPHeaders() {
        return this.userProvisioningAuthentication.getHeaders();
    }

    /**
     * *
     * Returns HTTP Header value
     *
     * @param key
     * @return
     */
    public String getHTTPHeader(String key) {
        return this.userProvisioningAuthentication.getHeader(key);
    }

    /**
     * *
     *
     * @return
     */
    /**
     * *
     * Add HTTP Header value to existing list of HTTP Headers
     *
     * @param key
     * @param value
     * @return
     */
    public UserProvisioningContext addHTTPHeader(String key, String value) {
        this.userProvisioningAuthentication.addHeader(key, value);
        return this;
    }

    public UserProvisioningContext setMode(Environment mode) {
        if (mode == null || !(mode.equals(Environment.PRODUCTION) || mode.equals(Environment.SANDBOX))) {
            throw new IllegalArgumentException(String.format("Mode needs to be either `%s` or `%s`.", Environment.SANDBOX, Environment.PRODUCTION));
        }
        userProvisioningAuthentication.addConfiguration(Constants.MODE, mode.name());
        return this;
    }

    /**
     * This will destroy the singleton object of UserProvisioningContext and
     * requests will no longer work
     */
    public static void destroySingletonObject() {
        UserProvisioningContext.instance = null;
    }
}
