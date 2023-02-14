package com.momo.api.base.context.collection;

import com.momo.api.base.constants.Constants;
import com.momo.api.base.exception.MoMoException;
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

    //TODO test case for addCallBackUrl and all similar functions that are required in integration test
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
    public CollectionConfiguration addCallBackUrl(String callBackUrl) throws MoMoException {
        this.callBackUrl = callBackUrl;
        if (CollectionContext.contextExists()) {
            createCollectionContext();
        }
        return this;
    }

    /**
     * Creates a singleton instance of CollectionContext. This
     * CollectionConfiguration constructor is used for Sandbox environment
     *
     * @param subscriptionKey
     * @param referenceId
     * @param apiKey
     */
    public CollectionConfiguration(String subscriptionKey, String referenceId, String apiKey) {
        this(subscriptionKey, referenceId, apiKey, Environment.SANDBOX, Constants.SANDBOX);
    }

    //TODO check if this constructor works as needed
    //TODO also check check if this works with sandbox environment
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
     */
    public CollectionConfiguration(String subscriptionKey, String referenceId, String apiKey, Environment mode, String targetEnvironment) {
        this(subscriptionKey, referenceId, apiKey, mode, targetEnvironment, null);
    }

    //TODO is there any point in passing the "configurations" as a parameter? Can it be modified?
    /**
     * Creates a singleton instance of CollectionContext.
     *
     * @param subscriptionKey
     * @param referenceId
     * @param apiKey
     * @param mode
     * @param targetEnvironment
     * @param configurations
     */
    public CollectionConfiguration(String subscriptionKey, String referenceId, String apiKey, Environment mode, String targetEnvironment, Map<String, String> configurations) {
        this.subscriptionKey = subscriptionKey;
        this.referenceId = referenceId;
        this.apiKey = apiKey;
        this.mode = mode;
        this.configurations = configurations;
        this.targetEnvironment = targetEnvironment;
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

    /**
     * This will destroy the singleton object of CollectionContext and requests
     * will no longer work
     */
    public static void destroySingletonObject() {
        CollectionContext.destroySingletonObject();
    }

}
