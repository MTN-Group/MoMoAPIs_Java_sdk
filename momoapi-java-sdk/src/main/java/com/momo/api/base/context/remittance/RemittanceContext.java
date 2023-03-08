package com.momo.api.base.context.remittance;

import com.momo.api.base.constants.Constants;
import com.momo.api.base.constants.SubscriptionType;
import com.momo.api.base.context.CommonContext;
import com.momo.api.base.context.MoMoAuthentication;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.model.HttpErrorResponse;
import com.momo.api.constants.Environment;
import java.util.Map;

/**
 *
 * Class RemittanceContext
 */
public class RemittanceContext extends CommonContext  {

    // Singleton instance
    private static RemittanceContext instance;

    private RemittanceContext(String subscriptionKey, String referenceId, String apiKey, Environment mode, String callBackUrl, Map<String, String> configurations, String targetEnvironment) throws MoMoException {
        this.credential = new MoMoAuthentication(subscriptionKey, SubscriptionType.REMITTANCE, referenceId, apiKey);
        if (configurations != null && configurations.size() > 0) {
            this.credential.addConfigurations(configurations);
        }

        this.setMode(mode);
        this.setTargetEnvironment(targetEnvironment);
        this.callBackUrl = callBackUrl;

        this.accessToken = this.credential.createAccessToken();
        instance = this;
    }

    /**
     * This is for creating the singleton context. Parameters "callBackUrl" and
     * "configurations" are not mandatory
     *
     * @param subscriptionKey
     * @param referenceId
     * @param apiKey
     * @param mode
     * @param callBackUrl
     * @param configurations
     * @param targetEnvironment
     * @throws MoMoException
     */
    public static void createContext(String subscriptionKey, String referenceId, String apiKey, Environment mode, String callBackUrl, Map<String, String> configurations, String targetEnvironment) throws MoMoException {
        if (instance == null) {
            synchronized (RemittanceContext.class) {
                if (instance == null) {
                    instance = new RemittanceContext(subscriptionKey, referenceId, apiKey, mode, callBackUrl, configurations, targetEnvironment);
                }
            }
        }
        instance.callBackUrl = callBackUrl;
    }

    /**
     * *
     * Returns context instance if exists or else throws exception
     *
     * @return
     * @throws MoMoException
     */
    public static RemittanceContext getContext() throws MoMoException {
        if (instance == null) {
            throw new MoMoException(
                    new HttpErrorResponse.HttpErrorResponseBuilder(Constants.INTERNAL_ERROR_CATEGORY,
                            Constants.GENERIC_ERROR_CODE).errorDescription(Constants.REQUEST_NOT_CREATED).build());
        } else {
            return instance;
        }
    }

    /**
     * *
     * Returns true if a context exists
     *
     * @return
     */
    public static boolean contextExists() {
        return instance != null;
    }

//    /**
//     * This will destroy the singleton object of RemittanceContext and requests
//     * will no longer work
//     */
//    public static void destroyContext() {
//        RemittanceContext.instance = null;
//    }
}
