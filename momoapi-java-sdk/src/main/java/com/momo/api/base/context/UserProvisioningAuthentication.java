package com.momo.api.base.context;

import com.momo.api.base.ConnectionManager;
import com.momo.api.base.HttpConfiguration;
import com.momo.api.base.HttpConnection;
import com.momo.api.base.HttpResponse;
import com.momo.api.base.constants.API;
import com.momo.api.base.constants.Constants;
import com.momo.api.base.constants.HttpMethod;
import com.momo.api.base.BaseAuthentication;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.model.StatusResponse;
import com.momo.api.base.util.JSONFormatter;
import com.momo.api.base.util.ResourceUtil;
import com.momo.api.models.ApiKey;
import com.momo.api.models.ApiUser;
import com.momo.api.models.CallbackHost;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Class UserProvisioningAuthentication
 */
public class UserProvisioningAuthentication extends BaseAuthentication {

    private final String subscriptionKey;
    CallbackHost providerCallbackHost;
    private String referenceId;

    /**
     * UserProvisioningAuthentication Constructor
     *
     * @param subscriptionKey
     */
    public UserProvisioningAuthentication(String subscriptionKey) {
        super();
        this.subscriptionKey = subscriptionKey;
    }

    /**
     * A new API User will be created and the ReferenceId(UUID) generated in
     * this method will be returned for generating access token later.
     *
     * @return
     * @throws MoMoException
     */
    private synchronized StatusResponse createUser() throws MoMoException {
        HttpConnection connection;
        HttpConfiguration httpConfiguration;

        try {
            connection = ConnectionManager.getInstance().getConnection();
            httpConfiguration = getOAuthHttpConfiguration(API.API_USER);
            connection.createAndConfigureHttpConnection(httpConfiguration);

            this.referenceId = UUID.randomUUID().toString();

            // Sets authorization header
            this.headers.put(Constants.X_REFERENCE_ID, this.referenceId);
            this.headers.put(Constants.SUBSCRIPTION_KEY, this.subscriptionKey);

            this.headers.put(Constants.HTTP_CONTENT_TYPE_HEADER, Constants.HTTP_CONTENT_TYPE_JSON);

            String payload = JSONFormatter.toJSON(this.providerCallbackHost);

            HttpResponse responseData = connection.execute(httpConfiguration.getEndPointUrl(), payload, this.headers);

            ResourceUtil.validateResponseData(responseData);

            StatusResponse statusResponse = new StatusResponse();
            statusResponse.setReferenceId(this.referenceId);
            statusResponse.setStatus(responseData.isSuccess());
            return statusResponse;
        } catch (IOException e) {
            Logger.getLogger(UserProvisioningAuthentication.class.getName()).log(Level.SEVERE, e.toString(), e);
        } finally {
            // Replace the headers back to JSON for any future use.
            this.headers.put(Constants.HTTP_CONTENT_TYPE_HEADER, Constants.HTTP_CONTENT_TYPE_JSON);
        }
        return null;
    }

    /**
     * Returns details of the API User based on the referenceId
     *
     * @param referenceId
     * @return
     * @throws MoMoException
     */
    public synchronized ApiUser getUserDetails(String referenceId) throws MoMoException {
        HttpConnection connection;
        HttpConfiguration httpConfiguration;

        try {
            connection = ConnectionManager.getInstance().getConnection();
            String url = API.X_REFERENCE_ID.replace(Constants.API_USER_REFERENCE_ID, referenceId);
            httpConfiguration = getOAuthHttpConfiguration(API.API_USER + url);
            httpConfiguration.setHttpMethod(HttpMethod.GET.toString());
            connection.createAndConfigureHttpConnection(httpConfiguration);

            // Sets authorization header
            //TODO check if header is removed correctly in case of exception
            this.headers.remove(Constants.X_REFERENCE_ID);
            this.headers.put(Constants.SUBSCRIPTION_KEY, this.subscriptionKey);

            this.headers.put(Constants.HTTP_CONTENT_TYPE_HEADER, Constants.HTTP_CONTENT_TYPE_JSON);

            HttpResponse responseData = connection.execute(httpConfiguration.getEndPointUrl(), "", this.headers);

            ResourceUtil.validateResponseData(responseData);

            if (responseData.getPayLoad() instanceof String) {
                ApiUser apiUser = JSONFormatter.fromJSON((String) responseData.getPayLoad(), ApiUser.class);
                return apiUser;
            }

            return null;
        } catch (IOException e) {
            Logger.getLogger(UserProvisioningAuthentication.class.getName()).log(Level.SEVERE, e.toString(), e);
        } finally {
            // Replace the headers back to JSON for any future use.
            this.headers.put(Constants.HTTP_CONTENT_TYPE_HEADER, Constants.HTTP_CONTENT_TYPE_JSON);
        }
        return null;
    }

    /**
     * Returns the ApiKey for the API User(referenceId/UUID) passed in
     *
     * @param referenceId
     * @return
     * @throws MoMoException
     */
    public synchronized ApiKey createApiKey(String referenceId) throws MoMoException {
        HttpConnection connection;
        HttpConfiguration httpConfiguration;

        try {
            connection = ConnectionManager.getInstance().getConnection();
            String url = API.API_KEY.replace(Constants.API_USER_REFERENCE_ID, referenceId);
            httpConfiguration = getOAuthHttpConfiguration(API.API_USER + url);
            connection.createAndConfigureHttpConnection(httpConfiguration);

            // Sets authorization header
            //TODO check if header is removed correctly in case of exception
            this.headers.remove(Constants.X_REFERENCE_ID);
            this.headers.put(Constants.SUBSCRIPTION_KEY, this.subscriptionKey);

            this.headers.put(Constants.HTTP_CONTENT_TYPE_HEADER, Constants.HTTP_CONTENT_TYPE_JSON);

            HttpResponse responseData = connection.execute(httpConfiguration.getEndPointUrl(), "", this.headers);

            ResourceUtil.validateResponseData(responseData);

            if (responseData.getPayLoad() instanceof String) {
                ApiKey apikey = JSONFormatter.fromJSON((String) responseData.getPayLoad(), ApiKey.class);
                return apikey;
            }

            return null;
        } catch (IOException e) {
            Logger.getLogger(UserProvisioningAuthentication.class.getName()).log(Level.SEVERE, e.toString(), e);
        } finally {
            // Replace the headers back to JSON for any future use.
            this.headers.put(Constants.HTTP_CONTENT_TYPE_HEADER, Constants.HTTP_CONTENT_TYPE_JSON);
        }
        return null;
    }

    /**
     * Creates a new API User
     *
     * @param callbackHost
     * @return
     * @throws MoMoException
     */
    public StatusResponse createUser(CallbackHost callbackHost) throws MoMoException {
        //TODO do we need to validate if "providerCallbackHost" value contain only the host value and not the complete url?
        this.providerCallbackHost = callbackHost;
        return createUser();
    }
}
