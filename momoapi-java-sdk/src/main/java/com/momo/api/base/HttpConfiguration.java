package com.momo.api.base;

import com.momo.api.base.constants.Constants;

public class HttpConfiguration {

    private String endPointUrl;
    private String httpMethod;
    private String ipAddress;
    private int connectionTimeout;
    private int readTimeout;
    private int maxRetry;
    private int maxHttpConnection;

    // Default constructor
    public HttpConfiguration() {
        this.maxRetry = 2;
        this.readTimeout = 0;
        this.connectionTimeout = 0;
        this.maxHttpConnection = 10;
        this.endPointUrl = null;
        this.ipAddress = "127.0.0.1";
        this.httpMethod = Constants.HTTP_CONFIG_DEFAULT_HTTP_METHOD;
    }

    /**
     * *
     *
     * @param httpMethod
     */
    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    /**
     * *
     *
     * @return
     */
    public String getHttpMethod() {
        return httpMethod;
    }

    /**
     * *
     *
     * @param endPointUrl
     */
    public void setEndPointUrl(String endPointUrl) {
        this.endPointUrl = endPointUrl;
    }

    /**
     * *
     *
     * @return
     */
    public String getEndPointUrl() {
        return endPointUrl;
    }

    /**
     * *
     *
     * @param connectionTimeout
     */
    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    /**
     * *
     *
     * @return
     */
    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * *
     *
     * @param maxRetry
     */
    public void setMaxRetry(int maxRetry) {
        this.maxRetry = maxRetry;
    }

    /**
     * *
     *
     * @return
     */
    public int getMaxRetry() {
        return this.maxRetry;
    }

    /**
     * *
     *
     * @param readTimeout
     */
    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    /**
     * *
     *
     * @return
     */
    public int getReadTimeout() {
        return readTimeout;
    }

    /**
     * *
     *
     * @param maxHttpConnection
     */
    public void setMaxHttpConnection(int maxHttpConnection) {
        this.maxHttpConnection = maxHttpConnection;
    }

    /**
     * *
     *
     * @return
     */
    public int getMaxHttpConnection() {
        return maxHttpConnection;
    }

    /**
     * *
     *
     * @param ipAddress
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
