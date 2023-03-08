package com.momo.api.models;

import java.io.Serializable;

/**
 *
 * Class CallbackHost
 */
public class CallbackHost implements Serializable {

    private static final long serialVersionUID = 1L;

    private String providerCallbackHost;

    public CallbackHost(String providerCallbackHost) {
        this.providerCallbackHost = providerCallbackHost;
    }

    /**
     * Returns the 'Host' callback URL registered in the server
     *
     * @return
     */
    public String getProviderCallbackHost() {
        return providerCallbackHost;
    }

    /**
     *
     * @param providerCallbackHost
     */
    public void setProviderCallbackHost(String providerCallbackHost) {
        this.providerCallbackHost = providerCallbackHost;
    }
}
