package collection;

import static base.SDKClient.get;
import static base.SDKClient.getSampleMSISDN;
import com.momo.api.base.constants.TargetEnvironment;
import com.momo.api.base.constants.IdType;
import com.momo.api.base.context.collection.CollectionConfiguration;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.model.StatusResponse;
import com.momo.api.base.util.JSONFormatter;
import com.momo.api.constants.Environment;
import com.momo.api.models.Payer;
import com.momo.api.models.collection.RequestPay;
import com.momo.api.models.collection.RequestPayStatus;
import com.momo.api.requests.collection.CollectionRequest;

/**
 *
 * Class RequestToPayTransactionStatus
 */
public class RequestToPayTransactionStatus {

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
            CollectionRequest collectionRequest = 
                    collectionConfiguration.createCollectionRequest();

            Payer payer = new Payer();
            payer.setPartyId(getSampleMSISDN());
            payer.setPartyIdType(IdType.MSISDN.getValue());

            RequestPay requestPay = new RequestPay();
            requestPay.setAmount("6.0");
            requestPay.setCurrency("EUR");
            requestPay.setExternalId("6353636");
            requestPay.setPayeeNote("payer note");
            requestPay.setPayerMessage("Pay for product a");
            requestPay.setPayer(payer);
            
            StatusResponse statusResponse = collectionRequest.requestToPay(requestPay);
            RequestPayStatus requestPayStatus = 
                    collectionRequest.requestToPayTransactionStatus(
                            statusResponse.getReferenceId()
                    );
            System.out.println("payStatus : " + JSONFormatter.toJSON(requestPayStatus));
        } catch (MoMoException ex) {
            System.out.println("MoMo Exception: " + ex.getMessage());
        }
    }
}
