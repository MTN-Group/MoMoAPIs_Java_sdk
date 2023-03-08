package com.momo.api.models;

/**
 *
 * Class ApiUser
 */
public class ApiUser extends CallbackHost {

    private static final long serialVersionUID = 1L;

    private String targetEnvironment;

    public ApiUser(String providerCallbackHost) {
        super(providerCallbackHost);
    }
    
    /**
     * Returns if the TargetEnvironment is sandbox or of production type
     *
     * @return
     */
    public String getTargetEnvironment() {
        return targetEnvironment;
    }

    /**
     *
     * @param targetEnvironment
     */
    public void setTargetEnvironment(String targetEnvironment) {
        this.targetEnvironment = targetEnvironment;
    }
}
