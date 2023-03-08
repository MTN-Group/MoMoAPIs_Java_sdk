package com.momo.api.base.context.remittance;

import com.momo.api.base.constants.TargetEnvironment;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.util.Validator;
import com.momo.api.constants.Environment;
import com.momo.api.requests.remittance.RemittanceRequest;
import java.util.Map;

/**
 *
 * Class RemittanceConfiguration
 */
public class RemittanceConfiguration {

    private String referenceId;
    private String apiKey;
    private String callBackUrl;
    private Environment mode;
    private Map<String, String> configurations;
    private String subscriptionKey;
    private String targetEnvironment;

    /**
     * This callBackURL will have a lower priority and will be overridden by the
     * callBackURL set for the RemittanceRequestObject.If the CallBackUrl is
     * added to an object of this class, then there is no need to specify the
     * CallBackUrl separately on each request object unless the callBackURL is
     * different for each request.
     *
     * @param callBackUrl
     * @return
     * @throws MoMoException
     */
    public RemittanceConfiguration addCallBackUrl(String callBackUrl) throws MoMoException {
        this.callBackUrl = callBackUrl;
        if (RemittanceContext.contextExists()) {
            createRemittanceContext();
        }
        return this;
    }

    /**
     * Creates a singleton instance of RemittanceContext. This
     * RemittanceConfiguration constructor is used for Production environment
     *
     * @param subscriptionKey
     * @param referenceId
     * @param apiKey
     * @throws MoMoException
     */
    public RemittanceConfiguration(String subscriptionKey, String referenceId, String apiKey) throws MoMoException {
        this(subscriptionKey, referenceId, apiKey, Environment.SANDBOX, TargetEnvironment.sandbox.getValue());
    }

    /**
     * Creates a singleton instance of RemittanceContext.This
     * RemittanceConfiguration constructor is used for Production environment,
     * but also can be used in sandbox if required
     *
     * @param subscriptionKey
     * @param referenceId
     * @param apiKey
     * @param mode
     * @param targetEnvironment
     * @throws MoMoException
     */
    public RemittanceConfiguration(String subscriptionKey, String referenceId, String apiKey, Environment mode, String targetEnvironment) throws MoMoException {
        this(subscriptionKey, referenceId, apiKey, mode, targetEnvironment, null);
    }

    /**
     * Creates a singleton instance of RemittanceContext.
     *
     * @param subscriptionKey
     * @param referenceId
     * @param apiKey
     * @param mode
     * @param targetEnvironment
     * @param configurations
     * @throws MoMoException
     */
    public RemittanceConfiguration(String subscriptionKey, String referenceId, String apiKey, Environment mode, String targetEnvironment, Map<String, String> configurations) throws MoMoException {
        this.subscriptionKey = subscriptionKey;
        this.referenceId = referenceId;
        this.apiKey = apiKey;
        this.mode = mode;
        this.configurations = configurations;
        this.targetEnvironment = targetEnvironment;
        Validator.configurationValidator(subscriptionKey, referenceId, apiKey, mode, targetEnvironment);
    }

    /**
     * creates a singleton instance of RemittanceContext and returns a new
     * RemittanceRequest object for making further requests in Remittance module
     *
     * @throws MoMoException
     * @return
     */
    public RemittanceRequest createRemittanceRequest() throws MoMoException {
        createRemittanceContext();
        return new RemittanceRequest();
    }

    /**
     * Creates a singleton instance of RemittanceContext.
     *
     * @throws MoMoException
     */
    private void createRemittanceContext() throws MoMoException {
        RemittanceContext.createContext(this.subscriptionKey, this.referenceId, this.apiKey, this.mode, this.callBackUrl, this.configurations, this.targetEnvironment);
    }

//    /**
//     * This will destroy the singleton object of RemittanceContext and requests
//     * will no longer work
//     */
//    public static void destroyContext() {
//        RemittanceContext.destroyContext();
//    }
}
