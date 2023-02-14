package com.momo.api.base.context.disbursement;

import com.momo.api.base.constants.Constants;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.constants.Environment;
import com.momo.api.requests.disbursement.DisbursementRequest;
import java.util.Map;

/**
 *
 * Class DisbursementConfiguration
 */
public class DisbursementConfiguration {

    private String referenceId;
    private String apiKey;
    private String callBackUrl;
    private Environment mode;
    private Map<String, String> configurations;
    private String subscriptionKey;
    private String targetEnvironment;

    /**
     * This callBackURL will have a lower priority and will be overridden by the
     * callBackURL set for the RequestObject.If the CallBackUrl is added to an
     * object of this class, then there is no need to specify the CallBackUrl
     * separately on each request object unless the callBackURL is different for
     * each request.
     *
     * @param callBackUrl
     * @return
     * @throws MoMoException
     */
    public DisbursementConfiguration addCallBackUrl(String callBackUrl) throws MoMoException {
        this.callBackUrl = callBackUrl;
        if (DisbursementContext.contextExists()) {
            createDisbursementContext();
        }
        return this;
    }

    /**
     * Creates a singleton instance of DisbursementContext.This
     * DisbursementConfiguration constructor is used for Production environment
     *
     * @param subscriptionKey
     * @param referenceId
     * @param apiKey
     */
    public DisbursementConfiguration(String subscriptionKey, String referenceId, String apiKey) {
        this(subscriptionKey, referenceId, apiKey, Environment.SANDBOX, Constants.SANDBOX);
    }

    /**
     * Creates a singleton instance of DisbursementContext.This
     * DisbursementConfiguration constructor is used for Production environment,
     * but also can be used in sandbox if required
     *
     * @param subscriptionKey
     * @param referenceId
     * @param apiKey
     * @param mode
     * @param targetEnvironment
     */
    public DisbursementConfiguration(String subscriptionKey, String referenceId, String apiKey, Environment mode, String targetEnvironment) {
        this(subscriptionKey, referenceId, apiKey, mode, targetEnvironment, null);
    }

    /**
     * Creates a singleton instance of DisbursementContext.
     *
     * @param subscriptionKey
     * @param referenceId
     * @param apiKey
     * @param mode
     * @param targetEnvironment
     * @param configurations
     */
    public DisbursementConfiguration(String subscriptionKey, String referenceId, String apiKey, Environment mode, String targetEnvironment, Map<String, String> configurations) {
        this.subscriptionKey = subscriptionKey;
        this.referenceId = referenceId;
        this.apiKey = apiKey;
        this.mode = mode;
        this.configurations = configurations;
        this.targetEnvironment = targetEnvironment;
    }

    /**
     * creates a singleton instance of DisbursementContext and returns a new
     * DisbursementRequest object for making further requests in Disbursement
     * module
     *
     * @throws MoMoException
     * @return
     */
    public DisbursementRequest createDisbursementRequest() throws MoMoException {
        createDisbursementContext();
        return new DisbursementRequest();
    }

    /**
     * Creates a singleton instance of DisbursementContext.
     *
     * @throws MoMoException
     */
    private void createDisbursementContext() throws MoMoException {
        DisbursementContext.createContext(this.subscriptionKey, this.referenceId, this.apiKey, this.mode, this.callBackUrl, this.configurations, this.targetEnvironment);
    }

    /**
     * This will destroy the singleton object of DisbursementContext and
     * requests will no longer work
     */
    public static void destroySingletonObject() {
        DisbursementContext.destroySingletonObject();
    }
}
