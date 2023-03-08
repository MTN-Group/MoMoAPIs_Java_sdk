package com.momo.api.base.model;

import com.momo.api.base.util.StringUtils;
import com.momo.api.models.Reason;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * *
 * Class HttpErrorResponse
 */
public final class HttpErrorResponse extends Reason {
    
    private static final long serialVersionUID = 1L;
    
    private String errorCategory;
    private String errorCode;
    private String errorDescription;
    private String errorDateTime;
    private List<HttpErrorMetaData> errorParameters;
    
    private String statusCode;
    
    private String error;

    /**
     * *
     * Constructor with builder object
     *
     * @param builder
     */
    private HttpErrorResponse(HttpErrorResponseBuilder builder) {
        this.errorCategory = builder.errorCategory;
        this.errorCode = builder.errorCode;
        this.errorDescription = builder.errorDescription;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        this.errorDateTime = StringUtils.isNullOrEmpty(builder.errorDateTime) ? formatter.format(new Date()) : builder.errorDateTime;
        this.errorParameters = builder.errorParameters;
    }

    /**
     * *
     * Constructor with HttpErrorReasonBuilder object
     *
     * @param builder
     */
    private HttpErrorResponse(HttpErrorReasonBuilder builder) {
        this.statusCode = builder.statusCode;
        this.code = builder.code;
        this.message = builder.message;
    }

    /**
     * *
     * Constructor with HttpErrorBuilder object
     *
     * @param builder
     */
    private HttpErrorResponse(HttpErrorBuilder builder) {
        this.error = builder.error;
        this.statusCode = builder.statusCode;
    }

    /**
     * *
     *
     * @return
     */
    public String getErrorCategory() {
        return errorCategory;
    }

    /**
     * *
     *
     * @return
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * *
     *
     * @return
     */
    public String getErrorDescription() {
        return errorDescription;
    }

    /**
     * *
     *
     * @return
     */
    public String getErrorDateTime() {
        return errorDateTime;
    }

    /**
     * *
     *
     * @return
     */
    public List<HttpErrorMetaData> getErrorParameters() {
        return errorParameters;
    }

    /**
     *
     * @return
     */
    public String getStatusCode() {
        return statusCode;
    }

    /**
     *
     * @param statusCode
     */
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    /**
     *
     * @return
     */
    public String getError() {
        return error;
    }

    /**
     *
     * @param error
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * *
     * Creates HttpErrorResponse object
     */
    public static class HttpErrorResponseBuilder {
        
        private String errorCategory;
        private String errorCode;
        private String errorDescription;
        private String errorDateTime;
        private List<HttpErrorMetaData> errorParameters;

        /**
         * *
         * Constructor with mandatory properties
         *
         * @param errorCategory
         * @param errorCode
         */
        public HttpErrorResponseBuilder(String errorCategory, String errorCode) {
            this.errorCategory = errorCategory;
            this.errorCode = errorCode;
        }

        /**
         * *
         *
         * @param errorDescription
         * @return
         */
        public HttpErrorResponseBuilder errorDescription(String errorDescription) {
            this.errorDescription = errorDescription;
            return this;
        }

        /**
         * *
         *
         * @param errorDateTime
         * @return
         */
        public HttpErrorResponseBuilder errorDateTime(String errorDateTime) {
            this.errorDateTime = errorDateTime;
            return this;
        }

        /**
         * *
         *
         * @param errorParameters
         * @return
         */
        public HttpErrorResponseBuilder errorParameters(List<HttpErrorMetaData> errorParameters) {
            this.errorParameters = errorParameters;
            return this;
        }

        /**
         * *
         * Returns constructed HttpErrorResponse object
         *
         * @return
         */
        public HttpErrorResponse build() {
            HttpErrorResponse httpErrorResponse = new HttpErrorResponse(this);
            return httpErrorResponse;
        }
    }

    /**
     * *
     * Creates HttpErrorResponse object
     */
    public static class HttpErrorReasonBuilder extends Reason {
        
        private static final long serialVersionUID = 1L;
        
        private String statusCode;

        /**
         *
         * @param statusCode
         * @param code
         */
        public HttpErrorReasonBuilder(String statusCode, String code) {
            this.statusCode = statusCode;
            this.code = code;
        }

        /**
         *
         * @param statusCode
         * @param code
         * @param message
         */
        public HttpErrorReasonBuilder(String statusCode, String code, String message) {
            this.statusCode = statusCode;
            this.code = code;
            this.message = message;
        }

        /**
         *
         * @param message
         * @return
         */
        public HttpErrorReasonBuilder message(String message) {
            this.message = message;
            return this;
        }

        /**
         *
         * @param code
         * @param message
         * @return
         */
        public HttpErrorReasonBuilder reason(String code, String message) {
            this.code = code;
            this.message = message;
            return this;
        }

        /**
         * *
         * Returns constructed HttpErrorResponse object
         *
         * @return
         */
        public HttpErrorResponse build() {
            HttpErrorResponse httpErrorResponse = new HttpErrorResponse(this);
            return httpErrorResponse;
        }
        
    }

    /**
     * *
     * Creates HttpErrorResponse object
     */
    public static class HttpErrorBuilder {
        
        private String statusCode;
        private String error;

        /**
         * *
         * Constructor with mandatory properties
         *
         * @param statusCode
         * @param error
         */
        public HttpErrorBuilder(String statusCode, String error) {
            this.statusCode = statusCode;
            this.error = error;
        }

        /**
         * *
         * Returns constructed HttpErrorResponse object
         *
         * @return
         */
        public HttpErrorResponse build() {
            HttpErrorResponse httpErrorResponse = new HttpErrorResponse(this);
            return httpErrorResponse;
        }
    }
}
