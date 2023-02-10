package com.momo.api.base.exception;
/**
 * 
 * Class SSLConfigurationException
 */
public class SSLConfigurationException extends BaseException {

    private static final long serialVersionUID = 1L;
    /***
     *
     * @param message
     */
    public SSLConfigurationException(String message) {
        super(message);
    }

    /***
     *
     * @param message
     * @param exception
     */
    public SSLConfigurationException(String message, Throwable exception) {
        super(message, exception);
    }
}
