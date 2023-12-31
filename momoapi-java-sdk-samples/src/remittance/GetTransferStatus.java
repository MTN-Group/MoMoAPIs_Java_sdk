package remittance;

import base.SDKClient;
import static base.SDKClient.get;
import com.momo.api.base.constants.TargetEnvironment;
import com.momo.api.base.constants.IdType;
import com.momo.api.base.context.remittance.RemittanceConfiguration;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.model.StatusResponse;
import com.momo.api.base.util.JSONFormatter;
import com.momo.api.constants.Environment;
import com.momo.api.models.Payee;
import com.momo.api.models.TransferStatus;
import com.momo.api.requests.remittance.RemittanceRequest;

/**
 *
 * Class GetTransferStatus
 */
public class GetTransferStatus {

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

            Payee payee = new Payee();
            payee.setPartyId(SDKClient.getSampleMSISDN());
            payee.setPartyIdType(IdType.MSISDN.getValue());

            com.momo.api.models.Transfer transfer = new com.momo.api.models.Transfer();
            transfer.setAmount("6.0");
            transfer.setCurrency("EUR");
            transfer.setExternalId("6353636");
            transfer.setPayeeNote("payer note");
            transfer.setPayerMessage("Pay for product a");
            transfer.setPayee(payee);

            StatusResponse statusResponse = remittanceRequest.transfer(transfer);

            TransferStatus transferStatus = 
                    remittanceRequest.getTransferStatus(
                            statusResponse.getReferenceId()
                    );
            System.out.println("transferStatus : " + JSONFormatter.toJSON(transferStatus));
        } catch (MoMoException ex) {
            System.out.println("MoMo Exception: " + ex.getMessage());
        }
    }
}
