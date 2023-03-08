package com.momo.api.base.context.collection;

import com.momo.api.base.constants.TargetEnvironment;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.util.Validator;
import com.momo.api.constants.Environment;
import com.momo.api.requests.collection.CollectionRequest;
import java.util.Map;

/**
 *
 * Class CollectionConfiguration
 */
public class CollectionConfiguration {

    private String referenceId;
    private String apiKey;
    private String callBackUrl;
    private Environment mode;
    private Map<String, String> configurations;
    private String subscriptionKey;
    private String targetEnvironment; // if we wish to give it explicitly, we can use this property

    /**
     * This callBackURL will have a lower priority and will be overridden by the
     * callBackURL set for the CollectionRequestObject.If the CallBackUrl is
     * added to an object of this class, then there is no need to specify the
     * CallBackUrl separately on each request object unless the callBackURL is
     * different for each request.
     *
     * @param callBackUrl
     * @return
     * @throws MoMoException
     */
    public CollectionConfiguration addCallBackUrl(String callBackUrl) throws MoMoException {
        this.callBackUrl = callBackUrl;
        if (CollectionContext.contextExists()) {
            createCollectionContext();
        }
        return this;
    }

    /**
     * Creates a singleton instance of CollectionContext.This
     * CollectionConfiguration constructor is used for Sandbox environment
     *
     * @param subscriptionKey
     * @param referenceId
     * @param apiKey
     * @throws MoMoException
     */
    public CollectionConfiguration(String subscriptionKey, String referenceId, String apiKey) throws MoMoException {
        this(subscriptionKey, referenceId, apiKey, Environment.SANDBOX, TargetEnvironment.sandbox.getValue());
    }

    /**
     * Creates a singleton instance of CollectionContext.This
     * CollectionConfiguration constructor is used for Production environment,
     * but also can be used in sandbox if required
     *
     * @param subscriptionKey
     * @param referenceId
     * @param apiKey
     * @param mode
     * @param targetEnvironment
     * @throws MoMoException
     */
    public CollectionConfiguration(String subscriptionKey, String referenceId, String apiKey, Environment mode, String targetEnvironment) throws MoMoException {
        this(subscriptionKey, referenceId, apiKey, mode, targetEnvironment, null);
    }

    /**
     * Creates a singleton instance of CollectionContext.
     *
     * @param subscriptionKey
     * @param referenceId
     * @param apiKey
     * @param mode
     * @param targetEnvironment
     * @param configurations
     * @throws MoMoException
     */
    public CollectionConfiguration(String subscriptionKey, String referenceId, String apiKey, Environment mode, String targetEnvironment, Map<String, String> configurations) throws MoMoException {
        this.subscriptionKey = subscriptionKey;
        this.referenceId = referenceId;
        this.apiKey = apiKey;
        this.mode = mode;
        this.configurations = configurations;
        this.targetEnvironment = targetEnvironment;
        Validator.configurationValidator(subscriptionKey, referenceId, apiKey, mode, targetEnvironment);
    }

    /**
     * creates a singleton instance of CollectionContext and returns a new
     * CollectionRequest object for making further requests in Collection module
     *
     * @throws MoMoException
     * @return
     */
    public CollectionRequest createCollectionRequest() throws MoMoException {
        createCollectionContext();
        return new CollectionRequest();
    }

    /**
     * Creates a singleton instance of CollectionContext.
     *
     * @throws MoMoException
     */
    private void createCollectionContext() throws MoMoException {
        CollectionContext.createContext(this.subscriptionKey, this.referenceId, this.apiKey, this.mode, this.callBackUrl,
                this.configurations, this.targetEnvironment);
    }

//    /**
//     * This will destroy the singleton object of CollectionContext and requests
//     * will no longer work
//     */
//    public static void destroyContext() {
//        CollectionContext.destroyContext();
//    }

}
