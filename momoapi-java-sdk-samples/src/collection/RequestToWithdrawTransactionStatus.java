package collection;

import base.SDKClient;
import static base.SDKClient.get;
import com.momo.api.base.constants.TargetEnvironment;
import com.momo.api.base.constants.IdType;
import com.momo.api.base.context.collection.CollectionConfiguration;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.model.StatusResponse;
import com.momo.api.base.util.JSONFormatter;
import com.momo.api.constants.Environment;
import com.momo.api.models.Payer;
import com.momo.api.models.collection.Withdraw;
import com.momo.api.models.collection.WithdrawStatus;
import com.momo.api.requests.collection.CollectionRequest;

/**
 *
 * Class RequestToWithdrawTransactionStatus
 */
public class RequestToWithdrawTransactionStatus {

    public static void main(String[] args) {
        try {
            CollectionConfiguration collectionConfiguration
                    = new CollectionConfiguration(
                            get("COLLECTION_SUBSCRIPTION_KEY"),
                            get("REFERENCE_ID"),
                            get("API_KEY"),
                            Environment.SANDBOX,
                            TargetEnvironment.sandbox.getValue())
                            .addCallBackUrl(
                                    get("CALLBACK_URL")
                            );
            CollectionRequest collectionRequest
                    = collectionConfiguration.createCollectionRequest();

            Payer payer = new Payer();
            payer.setPartyId(SDKClient.getSampleMSISDN());
            payer.setPartyIdType(IdType.MSISDN.getValue());

            Withdraw withdraw = new Withdraw();
            withdraw.setAmount("6.0");
            withdraw.setCurrency("EUR");
            withdraw.setExternalId("6353636");
            withdraw.setPayeeNote("payer note");
            withdraw.setPayerMessage("Pay for product a");
            withdraw.setPayer(payer);

            StatusResponse statusResponse = collectionRequest.requestToWithdrawV1(withdraw);
            
            WithdrawStatus withdrawStatus = collectionRequest.requestToWithdrawTransactionStatus(statusResponse.getReferenceId());
            System.out.println("withdrawStatus : " + JSONFormatter.toJSON(withdrawStatus));
            
            statusResponse = collectionRequest.requestToWithdrawV2(withdraw);
            
            withdrawStatus = collectionRequest.requestToWithdrawTransactionStatus(statusResponse.getReferenceId());
            System.out.println("withdrawStatus : " + JSONFormatter.toJSON(withdrawStatus));
        } catch (MoMoException ex) {
            System.out.println("MoMo Exception: " + ex.getMessage());
        }
    }

}
