package com.momo.api.models;

import java.io.Serializable;

/**
 *
 * Class Result
 */
public class Result implements Serializable{

    private static final long serialVersionUID = 1L;
    
    /**
     * 
     * Result
     * @param result
     */
    public Result(boolean result) {
        this.result = result;
    }
    
    private boolean result;

    /**
     * 
     * @return 
     */
    public boolean getResult() {
        return result;
    }

    /**
     * 
     * @param result 
     */
    public void setResult(boolean result) {
        this.result = result;
    }
}
