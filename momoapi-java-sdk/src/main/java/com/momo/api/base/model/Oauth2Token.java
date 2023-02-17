package com.momo.api.base.model;

/**
 *
 * Class Oauth2Token
 */
public class Oauth2Token extends AccessToken {

    private static final long serialVersionUID = 1L;

    private String scope;
    private String refresh_token;
    private String refresh_token_expires_in;

    /**
     * 
     * @return 
     */
    public String getScope() {
        return scope;
    }

    /**
     * 
     * @param scope 
     */
    public void setScope(String scope) {
        this.scope = scope;
    }

    /**
     * 
     * @return 
     */
    public String getRefresh_token() {
        return refresh_token;
    }

    /**
     * 
     * @param refresh_token 
     */
    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    /**
     * 
     * @return 
     */
    public String getRefresh_token_expires_in() {
        return refresh_token_expires_in;
    }

    /**
     * 
     * @param refresh_token_expires_in 
     */
    public void setRefresh_token_expires_in(String refresh_token_expires_in) {
        this.refresh_token_expires_in = refresh_token_expires_in;
    }

}
