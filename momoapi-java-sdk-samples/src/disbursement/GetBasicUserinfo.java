package disbursement;

import static base.SDKClient.get;
import static base.SDKClient.getSampleMSISDN;
import com.momo.api.base.constants.TargetEnvironment;
import com.momo.api.base.context.disbursement.DisbursementConfiguration;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.util.JSONFormatter;
import com.momo.api.constants.Environment;
import com.momo.api.models.BasicUserInfo;
import com.momo.api.requests.disbursement.DisbursementRequest;

/**
 *
 * Class GetBasicUserinfo
 */
public class GetBasicUserinfo {

    public static void main(String[] args) {
        try {
            DisbursementConfiguration disbursementConfiguration
                    = new DisbursementConfiguration(
                            get("DISBURSEMENT_SUBSCRIPTION_KEY"),
                            get("REFERENCE_ID"),
                            get("API_KEY"),
                            Environment.SANDBOX,
                            TargetEnvironment.sandbox.getValue());
            DisbursementRequest disbursementRequest = disbursementConfiguration.createDisbursementRequest();

            BasicUserInfo basicUserInfo = disbursementRequest.getBasicUserinfo(getSampleMSISDN());
            System.out.println("basicUserInfo : " + JSONFormatter.toJSON(basicUserInfo));
        } catch (MoMoException ex) {
            System.out.println("MoMo Exception: " + ex.getMessage());
        }
    }
}
