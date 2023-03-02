package remittance;

import static base.SDKClient.get;
import com.momo.api.base.constants.TargetEnvironment;
import com.momo.api.base.context.remittance.RemittanceConfiguration;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.util.JSONFormatter;
import com.momo.api.constants.Environment;
import com.momo.api.models.AccountBalance;
import com.momo.api.requests.remittance.RemittanceRequest;

/**
 *
 * Class GetAccountBalance
 */
public class GetAccountBalance {

    public static void main(String[] args) {
        try {
            RemittanceConfiguration remittanceConfiguration = new RemittanceConfiguration(
                    get("REMITTANCE_SUBSCRIPTION_KEY"),
                    get("REFERENCE_ID"),
                    get("API_KEY"),
                    Environment.SANDBOX,
                    TargetEnvironment.sandbox.getValue());
            RemittanceRequest remittanceRequest = remittanceConfiguration.createRemittanceRequest();

            AccountBalance accountBalance = remittanceRequest.getAccountBalance();
            System.out.println("accountBalance : " + JSONFormatter.toJSON(accountBalance));
        } catch (MoMoException ex) {
            System.out.println("MoMo Exception: " + ex.getMessage());
        }
    }
}
