package com.momo.api.base.exception;

import com.momo.api.base.model.HttpErrorResponse;
import com.momo.api.base.util.JSONFormatter;

/**
 *
 * Class MoMoException
 */
public class MoMoException extends Exception {

    private static final long serialVersionUID = 1L;

    private HttpErrorResponse error;

    /**
     * Default constructor
     *
     */
    public MoMoException() {
        super();
    }

    /**
     * Constructing exception with detailed message
     *
     * @param message
     */
    public MoMoException(String message) {
        super(message);
    }

    /**
     * Constructing exception with detailed message and cause
     *
     * @param message
     * @param cause
     */
    public MoMoException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor for passing in the error message to super class
     *
     * @param error
     */
    public MoMoException(HttpErrorResponse error) {
        super(JSONFormatter.toJSON(error));
        this.error = error;
    }

    /**
     * Returns the error message received from the request made
     *
     * @return
     */
    public HttpErrorResponse getError() {
        return error;
    }
}
