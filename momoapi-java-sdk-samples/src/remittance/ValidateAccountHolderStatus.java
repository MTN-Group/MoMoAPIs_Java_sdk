package remittance;

import static base.SDKClient.get;
import static base.SDKClient.getSampleMSISDN;
import com.momo.api.base.constants.TargetEnvironment;
import com.momo.api.base.constants.IdType;
import com.momo.api.base.context.remittance.RemittanceConfiguration;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.util.JSONFormatter;
import com.momo.api.constants.Environment;
import com.momo.api.models.AccountHolder;
import com.momo.api.models.Result;
import com.momo.api.requests.remittance.RemittanceRequest;

/**
 *
 * Class ValidateAccountHolderStatus
 */
public class ValidateAccountHolderStatus {

    public static void main(String[] args) {
        try {
            RemittanceConfiguration remittanceConfiguration = new RemittanceConfiguration(
                    get("REMITTANCE_SUBSCRIPTION_KEY"),
                    get("REFERENCE_ID"),
                    get("API_KEY"),
                    Environment.SANDBOX,
                    TargetEnvironment.sandbox.getValue())
                    .addCallBackUrl(
                            get("CALLBACK_URL")
                    );
            RemittanceRequest remittanceRequest = remittanceConfiguration.createRemittanceRequest();

            AccountHolder accountHolder = new AccountHolder(
                    IdType.msisdn.getValue(),
                    getSampleMSISDN()
            );

            Result result = remittanceRequest.validateAccountHolderStatus(accountHolder);
            System.out.println("result : " + JSONFormatter.toJSON(result));
        } catch (MoMoException ex) {
            System.out.println("MoMo Exception: " + ex.getMessage());
        }
    }
}
