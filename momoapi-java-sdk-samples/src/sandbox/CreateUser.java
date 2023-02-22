package sandbox;

import static base.SDKClient.get;
import com.momo.api.base.context.provisioning.UserProvisioningConfiguration;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.model.StatusResponse;
import com.momo.api.base.util.JSONFormatter;
import com.momo.api.models.CallbackHost;
import com.momo.api.requests.provisioning.UserProvisioningRequest;

/**
 *
 * Class CreateUser
 */
public class CreateUser {

    public static void main(String[] args) {
        try {
            CallbackHost callbackHost = new CallbackHost("webhook.site");
            UserProvisioningConfiguration userProvisioningConfiguration = 
                    new UserProvisioningConfiguration(
                            get("COLLECTION_SUBSCRIPTION_KEY")
                    );
            UserProvisioningRequest userProvisioningRequest = 
                    userProvisioningConfiguration.createUserProvisioningRequest();

            StatusResponse statusResponse = userProvisioningRequest.createUser(callbackHost);
            System.out.println("StatusResponse: " + JSONFormatter.toJSON(statusResponse));
        } catch (MoMoException ex) {
            System.out.println("MoMo Exception: "+ ex.getMessage());
        }
    }
}
