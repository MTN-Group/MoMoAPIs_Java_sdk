package com.momo.api.base.context.provisioning;

import com.momo.api.requests.provisioning.UserProvisioningRequest;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.constants.Environment;
import java.util.Map;

/**
 *
 * Class UserProvisioningConfiguration
 */
public class UserProvisioningConfiguration {

    private Environment mode;
    private Map<String, String> configurations;
    private String subscriptionKey;

    /**
     * UserProvisioningConfiguration constructor with only subscriptionKey
     *
     * @param subscriptionKey
     */
    public UserProvisioningConfiguration(String subscriptionKey) {
        this(subscriptionKey, Environment.SANDBOX);
    }

    /**
     * UserProvisioningConfiguration constructor with subscriptionKey and Environment
     *
     * @param subscriptionKey
     * @param mode
     */
    public UserProvisioningConfiguration(String subscriptionKey, Environment mode) {
        this(subscriptionKey, mode, null);
    }

    /**
     * UserProvisioningConfiguration constructor with subscriptionKey, Environment and
     * configurations
     *
     * @param subscriptionKey
     * @param mode
     * @param configurations
     */
    public UserProvisioningConfiguration(String subscriptionKey, Environment mode, Map<String, String> configurations) {
        this.subscriptionKey = subscriptionKey;
        this.mode = mode;
        this.configurations = configurations;
    }
    /**
     * For creating new ApiUser
     *
     * @return 
     * @throws MoMoException
     */
    public UserProvisioningRequest createUserProvisioningRequest() throws MoMoException {
        UserProvisioningContext.createContext(this.subscriptionKey, this.mode, this.configurations);
        return new UserProvisioningRequest();
    }

//    /**
//     * This will destroy the singleton object of UserProvisioningContext and
//     * requests will no longer work
//     */
//    public static void destroyContext() {
//        UserProvisioningContext.destroyContext();
//    }
}
