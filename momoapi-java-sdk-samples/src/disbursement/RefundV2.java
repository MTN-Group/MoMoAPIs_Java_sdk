package disbursement;

import static base.SDKClient.get;
import static base.SDKClient.getSampleMSISDN;
import com.momo.api.base.constants.TargetEnvironment;
import com.momo.api.base.constants.IdType;
import com.momo.api.base.context.collection.CollectionConfiguration;
import com.momo.api.base.context.disbursement.DisbursementConfiguration;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.model.StatusResponse;
import com.momo.api.base.util.JSONFormatter;
import com.momo.api.constants.Environment;
import com.momo.api.models.Payer;
import com.momo.api.models.collection.RequestPay;
import com.momo.api.models.disbursement.Refund;
import com.momo.api.requests.collection.CollectionRequest;
import com.momo.api.requests.disbursement.DisbursementRequest;

/**
 *
 * Class RefundV2
 */
public class RefundV2 {

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

            Refund refund = new Refund();
            refund.setAmount("6.0");
            refund.setCurrency("EUR");
            refund.setExternalId("6353636");
            refund.setPayeeNote("payer note");
            refund.setPayerMessage("Pay for product a");
            refund.setReferenceIdToRefund(statusResponse.getReferenceId());

            statusResponse = disbursementRequest.refundV2(refund);
            System.out.println("statusResponse : " + JSONFormatter.toJSON(statusResponse));
        } catch (MoMoException ex) {
            System.out.println("MoMo Exception: " + ex.getMessage());
        }
    }
}
