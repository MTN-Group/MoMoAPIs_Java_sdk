package com.momo.api.models;

import java.io.Serializable;

/**
 *
 * Class ApiKey
 */
public class ApiKey implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private String apiKey;

    /**
     * 
     * @return 
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * 
     * @param apiKey 
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
