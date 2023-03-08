package com.momo.api.models;

import java.io.Serializable;

/**
 *
 * Class Reason
 */
public class Reason implements Serializable{

    private static final long serialVersionUID = 1L;

    public String code;
    public String message;

    /**
     *
     * @return code
     */
    public String getCode() {
        return code;
    }

    /**
     *
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Returns the error message
     * 
     * @return 
     */
    public String getMessage() {
        return message;
    }

    /**
     * 
     * @param message 
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
