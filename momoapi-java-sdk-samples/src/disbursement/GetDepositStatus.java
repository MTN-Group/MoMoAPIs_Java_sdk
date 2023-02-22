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
import com.momo.api.models.disbursement.Deposit;
import com.momo.api.models.disbursement.DepositStatus;
import com.momo.api.requests.disbursement.DisbursementRequest;

/**
 *
 * Class GetDepositStatus
 */
public class GetDepositStatus {

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

            Deposit deposit = new Deposit();
            deposit.setAmount("6.0");
            deposit.setCurrency("EUR");
            deposit.setExternalId("6353636");
            deposit.setPayeeNote("payer note deposit");
            deposit.setPayerMessage("Pay for product a  deposit");
            deposit.setPayee(payee);

            StatusResponse statusResponse = disbursementRequest.depositV1(deposit);
            System.out.println("statusResponse : " + JSONFormatter.toJSON(statusResponse));

            DepositStatus depositStatus
                    = disbursementRequest.getDepositStatus(statusResponse.getReferenceId());
            System.out.println("depositStatus : " + JSONFormatter.toJSON(depositStatus));
            
            statusResponse = disbursementRequest.depositV2(deposit);
            System.out.println("statusResponse : " + JSONFormatter.toJSON(statusResponse));

            depositStatus
                    = disbursementRequest.getDepositStatus(statusResponse.getReferenceId());
            System.out.println("depositStatus : " + JSONFormatter.toJSON(depositStatus));
        } catch (MoMoException ex) {
            System.out.println("MoMo Exception: " + ex.getMessage());
        }
    }
}
