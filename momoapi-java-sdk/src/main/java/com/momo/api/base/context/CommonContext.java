package com.momo.api.base.context;

import com.momo.api.base.constants.Constants;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.model.AccessToken;
import com.momo.api.base.model.Oauth2Token;
import com.momo.api.constants.Environment;
import java.util.Map;

/**
 *
 * Class CommonContext
 */
public class CommonContext  implements MoMoContext {
    
    // Set global callback url
    protected String callBackUrl;
    
    protected MoMoAuthentication credential;
    protected AccessToken accessToken;
    protected Oauth2Token oauth2Token;
    
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
            this.accessToken = this.credential.createAccessToken();
            return this.accessToken;
        }
        return null;
    }

    /**
     * *
     * Returns access token
     *
     * @param auth_req_id
     * @return
     * @throws MoMoException
     */
    @Override
    public Oauth2Token fetchOauth2Token(String auth_req_id) throws MoMoException {
        if (this.credential != null) {
            this.oauth2Token = this.credential.createOauth2Token(auth_req_id);
            return this.oauth2Token;
        }
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
            this.accessToken = this.credential.getRefreshToken();
            return this.accessToken;
        }
        return null;
    }

    /**
     * *
     * Returns refresh token
     *
     * @param auth_req_id
     * @return
     * @throws MoMoException
     */
    @Override
    public Oauth2Token getRefreshOauth2Token(String auth_req_id) throws MoMoException {
        if (this.credential != null) {
            this.oauth2Token = this.credential.getRefreshOauth2Token(auth_req_id);
            return this.oauth2Token;
        }
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
    public CommonContext addHTTPHeader(String key, String value) {
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
    public CommonContext setMode(Environment mode) {
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
}
