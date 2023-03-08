package com.momo.api.base.model;

import java.io.Serializable;

/**
 *
 * Class AccessToken
 */
public class AccessToken implements Serializable {

    private static final long serialVersionUID = 1L;

    private String access_token;
    private String token_type;
    private String expires_in;

    /**
     * Constructor to prevent object creation
     */
    AccessToken() {
    }

    /**
     *
     * @return
     */
    public String getAccess_token() {
        return access_token;
    }

    /**
     *
     * @param access_token
     */
    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    /**
     *
     * @return
     */
    public String getToken_type() {
        return token_type;
    }

    /**
     *
     * @param token_type
     */
    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    /**
     *
     * @return
     */
    public String getExpires_in() {
        return expires_in;
    }

    /**
     *
     * @param expires_in
     */
    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }
}
