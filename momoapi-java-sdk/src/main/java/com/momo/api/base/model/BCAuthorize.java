package com.momo.api.base.model;

import java.io.Serializable;

/**
 *
 * Class BCAuthorize
 */
public class BCAuthorize implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String auth_req_id;
    private String expires_in;
    private String interval;

    /**
     *
     * @return
     */
    public String getAuth_req_id() {
        return auth_req_id;
    }

    /**
     *
     * @param auth_req_id
     */
    public void setAuth_req_id(String auth_req_id) {
        this.auth_req_id = auth_req_id;
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

    /**
     *
     * @return
     */
    public String getInterval() {
        return interval;
    }

    /**
     *
     * @param interval
     */
    public void setInterval(String interval) {
        this.interval = interval;
    }
}
