package com.momo.api.base.exception;

public class BaseException extends Exception {

    private static final long serialVersionUID = 1L;
    /***
     * Default constructor
     */
    public BaseException() { }

    /**
     *
     * @param msg
     */
    public BaseException(String msg) {
        super(msg);
    }

    /***
     *
     * @param msg
     * @param exception
     */
    public BaseException(String msg, Throwable exception) {
        super(msg, exception);
    }
}

