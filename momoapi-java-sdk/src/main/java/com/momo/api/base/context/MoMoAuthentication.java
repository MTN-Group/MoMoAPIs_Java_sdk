package com.momo.api.base.context;

import com.momo.api.base.BaseAuthentication;
import com.momo.api.base.ConnectionManager;
import com.momo.api.base.HttpConfiguration;
import com.momo.api.base.HttpConnection;
import com.momo.api.base.HttpResponse;
import com.momo.api.base.constants.API;
import com.momo.api.base.constants.Constants;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.model.AccessToken;
import com.momo.api.base.model.Oauth2Token;
import com.momo.api.base.util.JSONFormatter;
import com.momo.api.base.util.ResourceUtil;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Class MoMoAuthentication
 */
public class MoMoAuthentication extends BaseAuthentication {

    private final String subscriptionKey;
    private final String subscriptionType;
    private final String referenceId;
    private final String apiKey;

    private AccessToken accessToken;
    private Oauth2Token oauth2Token;

    public MoMoAuthentication(String subscriptionKey, String subscriptionType, String referenceId, String apiKey) {
        super();
        this.subscriptionKey = subscriptionKey;
        this.subscriptionType = subscriptionType;
        this.referenceId = referenceId;
        this.apiKey = apiKey;
    }

    /**
     * Returns AccessToken
     *
     * @return
     * @throws MoMoException
     */
    public AccessToken createAccessToken() throws MoMoException {
        return createAccessToken(this.accessToken == null || this.accessToken.getAccess_token() == null);
    }

    /**
     * Returns Oauth2Token
     *
     * @param auth_req_id
     * @return
     * @throws MoMoException
     */
    public Oauth2Token createOauth2Token(String auth_req_id) throws MoMoException {
        return createOauth2Token(this.oauth2Token == null || this.oauth2Token.getAccess_token() == null, auth_req_id);
    }

    /**
     * Returns RefreshToken
     *
     * @return
     * @throws MoMoException
     */
    public AccessToken getRefreshToken() throws MoMoException {
        this.accessToken = null;
        return createAccessToken();
    }

    /**
     * Returns Refresh Oauth2Token
     *
     * @param auth_req_id
     * @return
     * @throws MoMoException
     */
    public Oauth2Token getRefreshOauth2Token(String auth_req_id) throws MoMoException {
        this.oauth2Token = null;
        return createOauth2Token(auth_req_id);
    }

    /**
     * Generates AccessToken base on the value of subscriptionType
     *
     * @return
     * @throws MoMoException
     */
    private synchronized AccessToken createAccessToken(boolean createAccessToken) throws MoMoException {
        if(!createAccessToken){
            return this.accessToken;
        }
        HttpConnection connection;
        HttpConfiguration httpConfiguration;

        try {
            connection = ConnectionManager.getInstance().getConnection();
            String url = API.SUBSCRIPTION_TOKEN.replace(Constants.SUBSCRIPTION_TYPE, this.subscriptionType);
            httpConfiguration = getOAuthHttpConfiguration(url);
            connection.createAndConfigureHttpConnection(httpConfiguration);

            // Sets authorization header
            this.headers.put(Constants.SUBSCRIPTION_KEY, this.subscriptionKey);
            this.headers.put(Constants.AUTHORIZATION_HEADER, Constants.BASIC + generateBase64String());

            HttpResponse responseData = connection.execute(httpConfiguration.getEndPointUrl(), "", this.headers);

            ResourceUtil.validateResponseData(responseData, this.headers);

            if (responseData.getPayLoad() instanceof String) {
                this.accessToken = JSONFormatter.fromJSON((String) responseData.getPayLoad(), AccessToken.class);
                return this.accessToken;
            }

            return null;
        } catch (IOException e) {
            Logger.getLogger(MoMoAuthentication.class.getName()).log(Level.SEVERE, e.toString(), e);
        } finally {
            this.headers.put(Constants.HTTP_CONTENT_TYPE_HEADER, Constants.HTTP_CONTENT_TYPE_JSON);
        }
        return null;
    }

    /**
     * Generates Oauth2Token base on the value of subscriptionType
     *
     * @return
     * @throws MoMoException
     */
    private synchronized Oauth2Token createOauth2Token(boolean createOauth2Token, String auth_req_id) throws MoMoException {
        if(!createOauth2Token){
            return this.oauth2Token;
        }
        HttpConnection connection;
        HttpConfiguration httpConfiguration;

        try {
            connection = ConnectionManager.getInstance().getConnection();
            String url = API.SUBSCRIPTION_OAUTH2_TOKEN.replace(Constants.SUBSCRIPTION_TYPE, this.subscriptionType);
            httpConfiguration = getOAuthHttpConfiguration(url);
            connection.createAndConfigureHttpConnection(httpConfiguration);

            // Sets authorization header
            this.headers.put(Constants.SUBSCRIPTION_KEY, this.subscriptionKey);
            this.headers.put(Constants.AUTHORIZATION_HEADER, Constants.BASIC + generateBase64String());

            //will be reset in finally block
            this.headers.put(Constants.HTTP_CONTENT_TYPE_HEADER, Constants.HTTP_CONTENT_TYPE_URLENCODED);
            
            String payLoad = "grant_type=urn:openid:params:grant-type:ciba&auth_req_id="+auth_req_id;
            
            HttpResponse responseData = connection.execute(httpConfiguration.getEndPointUrl(), payLoad, this.headers);

            ResourceUtil.validateResponseData(responseData, this.headers);

            if (responseData.getPayLoad() instanceof String) {
                this.oauth2Token = JSONFormatter.fromJSON((String) responseData.getPayLoad(), Oauth2Token.class);
                return this.oauth2Token;
            }

            return null;
        } catch (IOException e) {
            Logger.getLogger(MoMoAuthentication.class.getName()).log(Level.SEVERE, e.toString(), e);
        } finally {
            this.headers.put(Constants.HTTP_CONTENT_TYPE_HEADER, Constants.HTTP_CONTENT_TYPE_JSON);
        }
        return null;
    }

    /**
     * *
     * Returns Base64 string generated after concatenating
     * referenceId(ApiUser/UUID) and apiKey
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    private String generateBase64String() throws UnsupportedEncodingException {
        String base64ClientID;
        byte[] encoded;
        try {
            encoded = Base64.getEncoder().encode((this.referenceId + ":" + this.apiKey).getBytes());
            base64ClientID = new String(encoded);
        } catch (Exception e) {
            throw new UnsupportedEncodingException(e.getMessage());
        }
        return base64ClientID;
    }
}
