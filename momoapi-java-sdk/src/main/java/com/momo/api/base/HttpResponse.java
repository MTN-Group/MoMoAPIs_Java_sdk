package com.momo.api.base;

import java.util.List;
import java.util.Map;

/***
 * Class HttpResponse
 */
public final class HttpResponse<T> {
    // Request response data
    private T payLoad;

    // Response status(success/failure)
    private boolean success;

    // Response code
    private HttpStatusCode responseCode;

    // Response headers
    private Map<String, List<String>> responseHeader;
    
    private String referenceId;

    /***
     * Private constructor with parameters
     *
     * @param payLoad
     * @param success
     * @param responseCode
     * @param responseHeader
     */
    private HttpResponse(T payLoad, boolean success, HttpStatusCode responseCode, Map<String, List<String>> responseHeader, String referenceId) {
        this.payLoad = payLoad;
        this.success = success;
        this.responseCode = responseCode;
        this.responseHeader = responseHeader;
        this.referenceId = referenceId;
    }

    /***
     * Creates HTTP response object
     *
     * @param payLoad
     * @param success
     * @param responseCode
     * @param responseHeader
     * @param referenceId
     * @return
     */
    public static <T> HttpResponse createResponse(T payLoad, boolean success, HttpStatusCode responseCode, Map<String, List<String>> responseHeader, String referenceId) {
        return new HttpResponse(payLoad, success, responseCode, responseHeader, referenceId);
    }

    /***
     * Set payLoad
     *
     * @param payLoad
     */
    public void setPayLoad(T payLoad) {
        this.payLoad = payLoad;
    }

    /***
     * Returns request payload data
     *
     * @return
     */
    public T getPayLoad() {
        return payLoad;
    }

    /***
     * Returns request status
     *
     * @return
     */
    public boolean isSuccess() {
        return success;
    }

    /***
     * Returns response code
     *
     * @return
     */
    public HttpStatusCode getResponseCode() {
        return responseCode;
    }

    /***
     * 
     * @return
     */
	public Map<String, List<String>> getResponseHeader() {
		return responseHeader;
	}

    public String getReferenceId() {
        return referenceId;
    }
    
}

