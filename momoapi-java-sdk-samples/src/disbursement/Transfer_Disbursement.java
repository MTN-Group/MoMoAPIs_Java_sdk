package disbursement;

import base.SDKClient;
import static base.SDKClient.get;
import com.momo.api.base.constants.TargetEnvironment;
import com.momo.api.base.constants.IdType;
import com.momo.api.base.context.disbursement.DisbursementConfiguration;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.model.StatusResponse;
import com.momo.api.base.util.JSONFormatter;
import com.momo.api.constants.Environment;
import com.momo.api.models.Payee;
import com.momo.api.models.Transfer;
import com.momo.api.requests.disbursement.DisbursementRequest;

/**
 *
 * Class Transfer_Disbursement
 */
public class Transfer_Disbursement {

    public static void main(String[] args) {
        try {
            DisbursementConfiguration disbursementConfiguration
                    = new DisbursementConfiguration(
                            get("DISBURSEMENT_SUBSCRIPTION_KEY"),
                            get("REFERENCE_ID"),
                            get("API_KEY"),
                            Environment.SANDBOX,
                            TargetEnvironment.sandbox.getValue())
                            .addCallBackUrl(
                                    get("CALLBACK_URL")
                            );
            DisbursementRequest disbursementRequest = disbursementConfiguration.createDisbursementRequest();

            Payee payee = new Payee();
            payee.setPartyId(SDKClient.getSampleMSISDN());
            payee.setPartyIdType(IdType.MSISDN.getValue());

            Transfer transfer = new com.momo.api.models.Transfer();
            transfer.setAmount("6.0");
            transfer.setCurrency("EUR");
            transfer.setExternalId("6353636");
            transfer.setPayeeNote("payer note");
            transfer.setPayerMessage("Pay for product a");
            transfer.setPayee(payee);

            StatusResponse statusResponse = disbursementRequest.transfer(transfer);
            System.out.println("statusResponse : " + JSONFormatter.toJSON(statusResponse));
        } catch (MoMoException ex) {
            System.out.println("MoMo Exception: " + ex.getMessage());
        }
    }
}
