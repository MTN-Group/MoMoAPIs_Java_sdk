package com.momo.api.base.model;

import java.io.Serializable;

/**
 *
 * Class StatusResponse
 */
public class StatusResponse extends BaseResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean status;

    /**
     * Returns the status of the request made
     *
     * @return
     */
    public boolean getStatus() {
        return status;
    }

    /**
     *
     * @param status
     */
    public void setStatus(boolean status) {
        this.status = status;
    }
}
