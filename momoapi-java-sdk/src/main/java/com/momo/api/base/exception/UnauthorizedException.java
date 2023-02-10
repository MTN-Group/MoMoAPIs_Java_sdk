package com.momo.api.base.exception;

/**
 * 
 * Class UnauthorizedException
 */
public class UnauthorizedException extends BaseException {

    private static final long serialVersionUID = 1L;
    /***
     * Default constructor
     */
    public UnauthorizedException() {
        super();
    }

    /***
     *
     * @param message
     */
    public UnauthorizedException(String message) {
        super(message);
    }

    /***
     *
     * @param message
     * @param exception
     */
    public UnauthorizedException(String message, Throwable exception) {
        super(message, exception);
    }
}

