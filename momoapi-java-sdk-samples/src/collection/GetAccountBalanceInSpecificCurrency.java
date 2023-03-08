package collection;

import static base.SDKClient.get;
import com.momo.api.base.constants.TargetEnvironment;
import com.momo.api.base.context.collection.CollectionConfiguration;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.util.JSONFormatter;
import com.momo.api.constants.Environment;
import com.momo.api.models.AccountBalance;
import com.momo.api.requests.collection.CollectionRequest;

/**
 *
 * Class GetAccountBalanceInSpecificCurrency
 */
public class GetAccountBalanceInSpecificCurrency {

    public static void main(String[] args) {
        try {
            CollectionConfiguration collectionConfiguration
                    = new CollectionConfiguration(
                            get("COLLECTION_SUBSCRIPTION_KEY"),
                            get("REFERENCE_ID"),
                            get("API_KEY"),
                            Environment.SANDBOX,
                            TargetEnvironment.sandbox.getValue());
            CollectionRequest collectionRequest
                    = collectionConfiguration.createCollectionRequest();

            AccountBalance accountBalance
                    = collectionRequest.getAccountBalanceInSpecificCurrency("EUR");
            System.out.println("accountBalance : " + JSONFormatter.toJSON(accountBalance));
        } catch (MoMoException ex) {
            System.out.println("MoMo Exception: " + ex.getMessage());
        }
    }
}
