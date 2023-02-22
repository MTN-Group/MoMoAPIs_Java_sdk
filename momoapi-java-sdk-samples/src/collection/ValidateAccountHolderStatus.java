package collection;

import static base.SDKClient.get;
import static base.SDKClient.getSampleMSISDN;
import com.momo.api.base.constants.TargetEnvironment;
import com.momo.api.base.constants.IdType;
import com.momo.api.base.context.collection.CollectionConfiguration;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.util.JSONFormatter;
import com.momo.api.constants.Environment;
import com.momo.api.models.AccountHolder;
import com.momo.api.models.Result;
import com.momo.api.requests.collection.CollectionRequest;

/**
 *
 * Class ValidateAccountHolderStatus
 */
public class ValidateAccountHolderStatus {

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

            AccountHolder accountHolder = new AccountHolder(
                    IdType.msisdn.getValue(),
                    getSampleMSISDN()
            );

            Result result = collectionRequest.validateAccountHolderStatus(accountHolder);
            System.out.println("result : " + JSONFormatter.toJSON(result));
        } catch (MoMoException ex) {
            System.out.println("MoMo Exception: " + ex.getMessage());
        }
    }
}
