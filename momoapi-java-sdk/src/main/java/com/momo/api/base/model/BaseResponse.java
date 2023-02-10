package com.momo.api.base.model;

import java.io.Serializable;

/**
 *
 * Class BaseResponse
 */
public class BaseResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String referenceId;

    /**
     * Returns the referenceID that was created for making this request
     *
     * @return
     */
    public String getReferenceId() {
        return referenceId;
    }

    /**
     *
     * @param referenceId
     */
    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }
}
