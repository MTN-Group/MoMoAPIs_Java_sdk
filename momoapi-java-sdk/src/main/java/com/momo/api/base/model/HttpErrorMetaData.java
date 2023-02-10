package com.momo.api.base.model;

import java.io.Serializable;

/**
 * *
 * Class HttpErrorMetaData
 */
public class HttpErrorMetaData implements Serializable{

    private static final long serialVersionUID = 1L;

    // Identifies the type of additional field
    private String key;

    // Identifies the value of the additional field
    private String value;

    /**
     * *
     *
     * @return
     */
    public String getKey() {
        return key;
    }

    /**
     * *
     *
     * @param key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * *
     *
     * @return
     */
    public String getValue() {
        return value;
    }

    /**
     * *
     *
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }
}
