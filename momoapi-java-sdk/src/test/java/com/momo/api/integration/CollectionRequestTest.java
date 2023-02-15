package com.momo.api.integration;

import com.momo.api.base.HttpStatusCode;
import com.momo.api.base.constants.Constants;
import com.momo.api.base.constants.IdType;
import com.momo.api.base.context.collection.CollectionConfiguration;
import com.momo.api.base.context.provisioning.UserProvisioningConfiguration;
import com.momo.api.base.context.provisioning.UserProvisioningContext;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.model.StatusResponse;
import com.momo.api.config.MSISDN;
import com.momo.api.config.PropertiesLoader;
import com.momo.api.constants.Environment;
import com.momo.api.constants.NotificationType;
import com.momo.api.models.AccountBalance;
import com.momo.api.models.AccountHolder;
import com.momo.api.models.ApiKey;
import com.momo.api.models.BasicUserInfo;
import com.momo.api.models.CallbackHost;
import com.momo.api.models.DeliveryNotification;
import com.momo.api.models.Payer;
import com.momo.api.models.Result;
import com.momo.api.models.collection.RequestPay;
import com.momo.api.models.collection.RequestPayStatus;
import com.momo.api.models.collection.Withdraw;
import com.momo.api.models.collection.WithdrawStatus;
import com.momo.api.requests.collection.CollectionRequest;
import com.momo.api.requests.provisioning.UserProvisioningRequest;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 *
 * Class CollectionRequestTest
 */
public class CollectionRequestTest {

    //TODO add comment for test methods
    //TODO test with MSISDN, EMAIL or PARTY_CODE
    //TODO write each case description in detail
    //TODO test if MODE(sandbox/prod), and other similar values are not causing any errors while creating an instance or modifying them
    static PropertiesLoader loader;

    @BeforeAll
    public static void init() {
        loader = new PropertiesLoader();
    }

    private final String incorrect_referenceId = "incorrect_referenceId";
    private final static String MSISDN_NUMBER = MSISDN.SUCCESSFUL.getValue();
    private final static String SUBSCRIPTION_KEY = "COLLECTION_SUBSCRIPTION_KEY";

    @Test
    @DisplayName("Call Back Url Test Success")
    public void callBackUrlTest() throws MoMoException {
        //Collection request made without calling "addCallBackUrl(String)" with "CollectionConfiguration" object
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX);
        CollectionRequest collectionRequestFirst = collectionConfiguration.createCollectionRequest();

        StatusResponse statusResponse;
        //case 0: Passing an incorrect CallBackUrl with the collectionConfiguration object. 
        //TODO: Don't do this, this will prevent future callbacks being received in the callBackUrl(tested in sandbox) used.
        //This only happens for requests made a few seconds(5sec) after callback with invalid url is made.
        //A new apiUser:apiKey combination will need to generated and used, for callBackUrl to work again later.
        //No callback will be received for any "CollectionRequest" object created, unless a valid callback url is set for "CollectionConfiguration" or for "CollectionRequest".
//        collectionConfiguration.addCallBackUrl(loader.get("CALLBACK_URL")+"invalid");
//        statusResponse = collectionRequestFirst.requestToPay(getPay("callBackUrlTest case 0"));

        //case 1: Passing a correct CallBackUrl with the collectionRequestFirst object. This allows callBack's to be received for all requests made with this "collectionRequestFirst" object.
        collectionRequestFirst.addCallBackUrl(loader.get("CALLBACK_URL"));
        statusResponse = collectionRequestFirst.requestToPay(getRequestPay(MSISDN_NUMBER));
        assertTrue(statusResponse.getStatus());

        //case 2: Passing NotificationType as "POLLING" with the collectionRequestFirst object. No callBack's will be received for any requests made with this "collectionRequestFirst" object unless the addCallBackUrl(String) is set again.
        collectionRequestFirst.setNotificationType(NotificationType.POLLING);
        statusResponse = collectionRequestFirst.requestToPay(getRequestPay(MSISDN_NUMBER));
        assertTrue(statusResponse.getStatus());

        //case 3: Passing a correct CallBackUrl with the collectionConfiguration object. But will not receive callBack's since the request is made with "collectionRequestFirst" having a "NotificationType" as "POLLING". To receive callback's again, we need to change "NotificationType" to "CALLBACK" or call the method "addCallBackUrl" with "collectionRequestFirst" with a valid callback url(as show in a later step below).
        collectionConfiguration.addCallBackUrl(loader.get("CALLBACK_URL"));
        statusResponse = collectionRequestFirst.requestToPay(getRequestPay(MSISDN_NUMBER));
        assertTrue(statusResponse.getStatus());

        //case 4: Creating a new collectionRequestNew object and passing valid callback url. Callback will be received.
        CollectionRequest collectionRequestNew = collectionConfiguration.createCollectionRequest();
        statusResponse = collectionRequestNew.requestToPay(getRequestPay(MSISDN_NUMBER));
        assertTrue(statusResponse.getStatus());

        //case 5: Correcting the CallBackUrl for "collectionRequestFirst" object will allow callBack's to be received again using this object.
        collectionRequestFirst.addCallBackUrl(loader.get("CALLBACK_URL"));
        statusResponse = collectionRequestFirst.requestToPay(getRequestPay(MSISDN_NUMBER));
        assertTrue(statusResponse.getStatus());
    }

    //TODO test methods that have reference_id as parameters, by passing a previously used reference id
    //TODO if required read object from jsonString: RequestToPay requestToPayFromString = JSONFormatter.fromJSON(requestToPayJsonString, RequestToPay.class); //only for success scenarios
    @Test
    @DisplayName("Request To Pay Test Success")
    void requestToPayTestSuccess() throws MoMoException {
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(loader.get("CALLBACK_URL"));
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        RequestPay pay = getRequestPay(MSISDN_NUMBER);

        StatusResponse statusResponse = collectionRequest.requestToPay(pay);
        assertNotNull(statusResponse);
        assertNotNull(statusResponse.getReferenceId());
        assertEquals(UUID.fromString(statusResponse.getReferenceId()).toString(), statusResponse.getReferenceId());
        assertTrue(statusResponse.getStatus());
    }

    @Test
    @DisplayName("Request To Pay Test Failure")
    void requestToPayTestFailure() throws MoMoException {
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(loader.get("CALLBACK_URL"));
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        //case 1: Pay object is null
        MoMoException moMoException = assertThrows(MoMoException.class, () -> collectionRequest.requestToPay(null));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.PAY_OBJECT_INIT_ERROR);

        //case 2: Pay object initialised, but varialbes are not defined
        RequestPay requestPay = new RequestPay();
        moMoException = assertThrows(MoMoException.class, () -> collectionRequest.requestToPay(requestPay));
        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.BAD_REQUEST.getHttpStatusCode()));

        //case 3: Payer object initialised, but varialbes are not defined
        requestPay.setPayer(new Payer());
        moMoException = assertThrows(MoMoException.class, () -> collectionRequest.requestToPay(requestPay));
        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.INTERNAL_SERVER_ERROR.getHttpStatusCode()));
    }

    @Test
    @DisplayName("Request To Pay Transaction Status Test Success")
    void requestToPayTransactionStatusTestSuccess() throws MoMoException {
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(loader.get("CALLBACK_URL"));
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        RequestPay requestPay = getRequestPay(MSISDN_NUMBER);

        StatusResponse statusResponse = collectionRequest.requestToPay(requestPay);

        RequestPayStatus requestPayStatus = collectionRequest.requestToPayTransactionStatus(statusResponse.getReferenceId());

        assertNotNull(requestPayStatus);
        assertNotNull(requestPayStatus.getAmount());
        assertNotNull(requestPayStatus.getCurrency());
        assertNotNull(requestPayStatus.getExternalId());
        assertNotNull(requestPayStatus.getFinancialTransactionId());
        assertNotNull(requestPayStatus.getPayeeNote());
        assertNotNull(requestPayStatus.getPayerMessage());
        //TODO check Reason object is correct
        //assertNotNull(requestPayStatus.getReason());
        assertNotNull(requestPayStatus.getStatus());
        assertTrue(requestPayStatus.getStatus().equals("SUCCESSFUL"));

        assertNotNull(requestPayStatus.getPayer());
        Payer payer = requestPayStatus.getPayer();
        assertNotNull(payer.getPartyId());
        assertNotNull(payer.getPartyIdType());
    }

    @Test
    @DisplayName("Request To Pay Transaction Status Test Failure")
    void requestToPayTransactionStatusTestFailure() throws MoMoException {
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(loader.get("CALLBACK_URL"));
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        //case 1: referenceId is null
        MoMoException moMoException = assertThrows(MoMoException.class, () -> collectionRequest.requestToPayTransactionStatus(null));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.NULL_VALUE_ERROR);

        //case 2: referenceId is empty
        moMoException = assertThrows(MoMoException.class, () -> collectionRequest.requestToPayTransactionStatus(""));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.EMPTY_STRING_ERROR);

        //case 3: incorrect referenceId provided
        moMoException = assertThrows(MoMoException.class, () -> collectionRequest.requestToPayTransactionStatus(incorrect_referenceId));
        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.BAD_REQUEST.getHttpStatusCode()));
    }

    @Test
    @DisplayName("Get Account Balance Test Success")
    void getAccountBalanceTestSuccess() throws MoMoException {
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX);
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        AccountBalance accountBalance = collectionRequest.getAccountBalance();
        assertNotNull(accountBalance);
        assertNotNull(accountBalance.getAvailableBalance());
        assertNotNull(accountBalance.getCurrency());
    }

    /**
     * Making an API request without creating context
     *
     * @throws MoMoException
     */
    @Test
    @DisplayName("Get Account Balance Test Failure")
    void getAccountBalanceTestFailure() throws MoMoException {
        //Destroying the existing context to check of requests fail
        CollectionConfiguration.destroySingletonObject();
        //case 1: CollectionRequest is not properly initialised
        CollectionRequest collectionRequest = new CollectionRequest();
        MoMoException moMoException = assertThrows(MoMoException.class, () -> collectionRequest.getAccountBalance());
        assertEquals(moMoException.getError().getErrorDescription(), Constants.REQUEST_NOT_CREATED);
    }

    @Test
    @DisplayName("Get Account Balance In Specific Currency Test Success")
    void getAccountBalanceInSpecificCurrencyTestSuccess() throws MoMoException {
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX);
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        AccountBalance accountBalance = collectionRequest.getAccountBalanceInSpecificCurrency("EUR");
        assertNotNull(accountBalance);
        assertNotNull(accountBalance.getAvailableBalance());
        assertNotNull(accountBalance.getCurrency());
    }

    @Test
    @DisplayName("Get Account Balance In Specific Currency Test Failure")
    void getAccountBalanceInSpecificCurrencyTestFailure() throws MoMoException {
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX);
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        //TODO some methods donot accept NULL valies directly, and throws compile tome error. check which method it is.
        //case 1: currency is null
        MoMoException moMoException = assertThrows(MoMoException.class, () -> collectionRequest.getAccountBalanceInSpecificCurrency(null));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.NULL_VALUE_ERROR);

        //case 2: currency is empty
        moMoException = assertThrows(MoMoException.class, () -> collectionRequest.getAccountBalanceInSpecificCurrency(""));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.EMPTY_STRING_ERROR);
    }

    @Test
    @DisplayName("Request To Pay Delivery Notification Test Success")
    void requestToPayDeliveryNotificationTestSuccess() throws MoMoException {
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(loader.get("CALLBACK_URL"));
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        RequestPay requestPay = getRequestPay(MSISDN_NUMBER);
        StatusResponse statusResponsePay = collectionRequest.requestToPay(requestPay);

        DeliveryNotification deliveryNotification = new DeliveryNotification();
        deliveryNotification.setNotificationMessage("test message");

        StatusResponse statusResponseDeliveryNotification = collectionRequest.requestToPayDeliveryNotification(statusResponsePay.getReferenceId(), deliveryNotification);
        assertEquals(statusResponseDeliveryNotification.getStatus(), true);

        statusResponsePay = collectionRequest.requestToPay(requestPay);

        statusResponseDeliveryNotification = collectionRequest.requestToPayDeliveryNotification(statusResponsePay.getReferenceId(), deliveryNotification, "Header Message");
        assertEquals(statusResponseDeliveryNotification.getStatus(), true);
    }

    @Test
    @DisplayName("Request To Pay Delivery Notification Test Failure")
    void requestToPayDeliveryNotificationTestFailure() throws MoMoException {
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(loader.get("CALLBACK_URL"));
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        RequestPay requestPay = getRequestPay(MSISDN_NUMBER);
        StatusResponse statusResponsePay = collectionRequest.requestToPay(requestPay);
        DeliveryNotification deliveryNotification = new DeliveryNotification();
        MoMoException moMoException;

        //case 1: referenceId and deliveryNotification is null
        moMoException = assertThrows(MoMoException.class, () -> collectionRequest.requestToPayDeliveryNotification(null, null));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.NULL_VALUE_ERROR);

        //case 2: referenceId and deliveryNotification is blank
        moMoException = assertThrows(MoMoException.class, () -> collectionRequest.requestToPayDeliveryNotification("", null));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.EMPTY_STRING_ERROR);

        //case 3: DeliveryNotification is null
        moMoException = assertThrows(MoMoException.class, () -> collectionRequest.requestToPayDeliveryNotification(statusResponsePay.getReferenceId(), null));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.DELIVERY_NOTIFICATION_OBJECT_INIT_ERROR);

        //case 4: DeliveryNotification object initialised, but varialbes are not defined
        moMoException = assertThrows(MoMoException.class, () -> collectionRequest.requestToPayDeliveryNotification(statusResponsePay.getReferenceId(), deliveryNotification));
        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.BAD_REQUEST.getHttpStatusCode()));

        deliveryNotification.setNotificationMessage("test message");

        //case 5: NotificationMessage string in header is null
        moMoException = assertThrows(MoMoException.class, () -> collectionRequest.requestToPayDeliveryNotification(statusResponsePay.getReferenceId(), deliveryNotification, null));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.NULL_VALUE_ERROR);

        //TODO Header:-Do we need to validate the notification message string in header to be not more than 160 characters? No error is thrown during the actual API call
        //case _: NotificationMessage string in header is more than 160 characters
//        moMoException = assertThrows(MoMoException.class, ()->collectionRequest.requestToPayDeliveryNotification(statusResponsePay.getReferenceId(), deliveryNotification, stringLength_161));
//        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.BAD_REQUEST.getHttpStatusCode()));
        //TODO Body:-Do we need to validate the notification message string to be not more than 160 characters? No error is thrown during the actual API call
        //161 character string
        String stringLength_161 = "01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";

        //case 6: NotificationMessage string in DeliveryNotification object is more than 160 characters
        deliveryNotification.setNotificationMessage(stringLength_161);
        moMoException = assertThrows(MoMoException.class, () -> collectionRequest.requestToPayDeliveryNotification(statusResponsePay.getReferenceId(), deliveryNotification));
        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.BAD_REQUEST.getHttpStatusCode()));

        //case 7: Using the same referenceId for requestToPayDeliveryNotification after a successful request was made
        deliveryNotification.setNotificationMessage("test message");
        //making the first request
        StatusResponse statusResponseDeliveryNotification = collectionRequest.requestToPayDeliveryNotification(statusResponsePay.getReferenceId(), deliveryNotification, "Header Message");
        assertEquals(statusResponseDeliveryNotification.getStatus(), true);
        //making the second request
        moMoException = assertThrows(MoMoException.class, () -> collectionRequest.requestToPayDeliveryNotification(statusResponsePay.getReferenceId(), deliveryNotification, "Header Message"));
        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.TOO_MANY_REQUESTS.getHttpStatusCode()));

    }

    @Test
    @DisplayName("Validate Account Holder Status Test Success")
    void validateAccountHolderStatusTestSuccess() throws MoMoException {
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX);
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        AccountHolder accountHolder = new AccountHolder(IdType.MSISDN.getIdTypeLowerCase(), MSISDN_NUMBER);
        Result result = collectionRequest.validateAccountHolderStatus(accountHolder);
        assertNotNull(result);
        assertTrue(result.getResult());
    }

    @Test
    @DisplayName("Validate Account Holder Status Test Failure")
    void validateAccountHolderStatusTestFailure() throws MoMoException {
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX);
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        MoMoException moMoException;

        //case 1: AccountHolder is null
        moMoException = assertThrows(MoMoException.class, () -> collectionRequest.validateAccountHolderStatus(null));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.ACCOUNT_HOLDER_OBJECT_INIT_ERROR);

        //case 2: Key or Value is null
        moMoException = assertThrows(MoMoException.class, () -> collectionRequest.validateAccountHolderStatus(new AccountHolder(IdType.MSISDN.getIdTypeLowerCase(), null)));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.NULL_VALUE_ERROR);

        //case 3: Key or Value is empty
        moMoException = assertThrows(MoMoException.class, () -> collectionRequest.validateAccountHolderStatus(new AccountHolder("", MSISDN_NUMBER)));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.EMPTY_STRING_ERROR);
    }

    @Test
    @DisplayName("Request To Withdraw V1 Test Success")
    void requestToWithdrawV1TestSuccess() throws MoMoException {
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(loader.get("CALLBACK_URL"));
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        Withdraw withdraw = getWithdraw(MSISDN_NUMBER);

        StatusResponse statusResponse = collectionRequest.requestToWithdrawV1(withdraw);
        assertNotNull(statusResponse);
        assertNotNull(statusResponse.getReferenceId());
        assertTrue(statusResponse.getStatus());
    }

    @Test
    @DisplayName("Request To Withdraw V1 Test Failure")
    void requestToWithdrawV1TestFailure() throws MoMoException {
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(loader.get("CALLBACK_URL"));
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        //case 1: Withdraw object is null
        MoMoException moMoException = assertThrows(MoMoException.class, () -> collectionRequest.requestToWithdrawV1(null));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.WITHDRAW_OBJECT_INIT_ERROR);

        //case 2: Withdraw object initialised, but varialbes are not defined
        Withdraw withdraw = new Withdraw();
        moMoException = assertThrows(MoMoException.class, () -> collectionRequest.requestToWithdrawV1(withdraw));
        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.BAD_REQUEST.getHttpStatusCode()));

        //case 3: Payer object initialised, but varialbes are not defined
        withdraw.setPayer(new Payer());
        moMoException = assertThrows(MoMoException.class, () -> collectionRequest.requestToWithdrawV1(withdraw));
        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.INTERNAL_SERVER_ERROR.getHttpStatusCode()));
    }

    @Test
    @DisplayName("Request To Withdraw V2 Test Success")
    void requestToWithdrawV2TestSuccess() throws MoMoException {
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(loader.get("CALLBACK_URL"));
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        Withdraw withdraw = getWithdraw(MSISDN_NUMBER);

        StatusResponse statusResponse = collectionRequest.requestToWithdrawV2(withdraw);
        assertNotNull(statusResponse);
        assertNotNull(statusResponse.getReferenceId());
        assertTrue(statusResponse.getStatus());
    }

    @Test
    @DisplayName("Request To Withdraw V2 Test Failure")
    void requestToWithdrawV2TestFailure() throws MoMoException {
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(loader.get("CALLBACK_URL"));
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        //case 1: Withdraw object is null
        MoMoException moMoException = assertThrows(MoMoException.class, () -> collectionRequest.requestToWithdrawV2(null));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.WITHDRAW_OBJECT_INIT_ERROR);

        //case 2: Withdraw object initialised, but varialbes are not defined
        Withdraw withdraw = new Withdraw();
        moMoException = assertThrows(MoMoException.class, () -> collectionRequest.requestToWithdrawV2(withdraw));
        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.BAD_REQUEST.getHttpStatusCode()));

        //case 3: Payer object initialised, but varialbes are not defined
        withdraw.setPayer(new Payer());
        moMoException = assertThrows(MoMoException.class, () -> collectionRequest.requestToWithdrawV2(withdraw));
        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.INTERNAL_SERVER_ERROR.getHttpStatusCode()));
    }

    @Test
    @DisplayName("Request To Withdraw Transaction Status Test Success")
    void requestToWithdrawTransactionStatusTestSuccess() throws MoMoException {
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(loader.get("CALLBACK_URL"));
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        Withdraw withdraw = getWithdraw(MSISDN_NUMBER);

        StatusResponse statusResponse = collectionRequest.requestToWithdrawV1(withdraw);

        WithdrawStatus withdrawStatus = collectionRequest.requestToWithdrawTransactionStatus(statusResponse.getReferenceId());

        assertNotNull(withdrawStatus);
        assertNotNull(withdrawStatus.getAmount());
        assertNotNull(withdrawStatus.getCurrency());
        assertNotNull(withdrawStatus.getExternalId());
        assertNotNull(withdrawStatus.getFinancialTransactionId());
        assertNotNull(withdrawStatus.getPayeeNote());
        assertNotNull(withdrawStatus.getPayerMessage());
        //TODO check Reason object is correct
        //assertNotNull(payStatus.getReason());
        //TODO assert possible status values
        assertNotNull(withdrawStatus.getStatus());

        assertNotNull(withdrawStatus.getPayer());
        Payer payer = withdrawStatus.getPayer();
        assertNotNull(payer.getPartyId());
        assertNotNull(payer.getPartyIdType());
    }

    @Test
    @DisplayName("Request To Withdraw Transaction Status Test Failure")
    void requestToWithdrawTransactionStatusTestFailure() throws MoMoException {
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(loader.get("CALLBACK_URL"));
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        //case 1: referenceId is null
        MoMoException moMoException = assertThrows(MoMoException.class, () -> collectionRequest.requestToWithdrawTransactionStatus(null));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.NULL_VALUE_ERROR);

        //case 2: referenceId is empty
        moMoException = assertThrows(MoMoException.class, () -> collectionRequest.requestToWithdrawTransactionStatus(""));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.EMPTY_STRING_ERROR);

        //case 3: incorrect referenceId provided
        moMoException = assertThrows(MoMoException.class, () -> collectionRequest.requestToWithdrawTransactionStatus(incorrect_referenceId));
        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.BAD_REQUEST.getHttpStatusCode()));
    }

    @Test
    @DisplayName("Get Basic Userinfo Test Success")
    void getBasicUserinfoTestSuccess() throws MoMoException {
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX);
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        BasicUserInfo basicUserInfo = collectionRequest.getBasicUserinfo(MSISDN_NUMBER);
        assertNotNull(basicUserInfo);
        assertNotNull(basicUserInfo.getBirthdate());
        assertNotNull(basicUserInfo.getFamily_name());
        assertNotNull(basicUserInfo.getGender());
        assertNotNull(basicUserInfo.getGiven_name());
        assertNotNull(basicUserInfo.getLocale());
//        assertNotNull(basicUserInfo.getStatus());
    }

    @Test
    @DisplayName("Get Basic Userinfo Test Failure")
    void getBasicUserinfoTestFailure() throws MoMoException {
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX);
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        MoMoException moMoException;

        //case 1: Value is null
        moMoException = assertThrows(MoMoException.class, () -> collectionRequest.getBasicUserinfo(null));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.NULL_VALUE_ERROR);

        //case 2: Value is empty
        moMoException = assertThrows(MoMoException.class, () -> collectionRequest.getBasicUserinfo(""));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.EMPTY_STRING_ERROR);
    }

    @Test
    @DisplayName("Callback Host Test Success")
    void callbackHostTestSuccess() throws MoMoException {
        CallbackHost callbackHost = new CallbackHost("webhook.site");
        UserProvisioningConfiguration userProvisioningConfiguration = new UserProvisioningConfiguration(loader.get(SUBSCRIPTION_KEY));
        UserProvisioningRequest userProvisioningRequest = userProvisioningConfiguration.createUserProvisioningRequest();

        StatusResponse statusResponse = userProvisioningRequest.createUser(callbackHost);

        ApiKey apiKey = userProvisioningRequest.createApiKey(statusResponse.getReferenceId());

        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), statusResponse.getReferenceId(), apiKey.getApiKey(), Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(loader.get("CALLBACK_URL"));
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        RequestPay requestPay = getRequestPay(MSISDN_NUMBER);

        statusResponse = collectionRequest.requestToPay(requestPay);
        assertNotNull(statusResponse);
        assertNotNull(statusResponse.getReferenceId());
        assertEquals(UUID.fromString(statusResponse.getReferenceId()).toString(), statusResponse.getReferenceId());
        assertTrue(statusResponse.getStatus());
    }

    /**
     * Checking if providing a callBackUrl with different host throws error 
     *
     * @throws MoMoException
     */
    @Test
    @DisplayName("Callback Host Test Failure")
    void callbackHostTestFailure() throws MoMoException {
        //destroying the exising valid UserProvisioning singleton instance, to create new instance with invalid "CallbackHost"
        UserProvisioningContext.destroySingletonObject();
        //CallbackHost is "something.site", so the "callBackUrl" must be a url with "something.site" as its host address
        CallbackHost callbackHost = new CallbackHost("something.site");
        UserProvisioningConfiguration userProvisioningConfiguration = new UserProvisioningConfiguration(loader.get(SUBSCRIPTION_KEY));
        UserProvisioningRequest userProvisioningRequest = userProvisioningConfiguration.createUserProvisioningRequest();

        StatusResponse statusResponse = userProvisioningRequest.createUser(callbackHost);

        ApiKey apiKey = userProvisioningRequest.createApiKey(statusResponse.getReferenceId());
        //destroying the newly created singleton instance having invalid "CallbackHost" value. Now a new instance can be created
        UserProvisioningContext.destroySingletonObject();

        //destroying the exising collection singleton instance, to create new instance with the above parameters 
        CollectionConfiguration.destroySingletonObject();
        //here 'loader.get("CALLBACK_URL")' need to contain a callBackUrl with a host address different than "something.site", so that it throws error
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), statusResponse.getReferenceId(), apiKey.getApiKey(), Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(loader.get("CALLBACK_URL"));
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        RequestPay requestPay = getRequestPay(MSISDN_NUMBER);

        MoMoException moMoException = assertThrows(MoMoException.class, () -> collectionRequest.requestToPay(requestPay));
        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.INTERNAL_SERVER_ERROR.getHttpStatusCode()));
        //destroying the newly created collection singleton instance. Now a new instance can be created
        CollectionConfiguration.destroySingletonObject();
    }

    private static Payer getPayer(String msisdnValue) {
        Payer payer = new Payer();
        payer.setPartyId(msisdnValue);
        payer.setPartyIdType(IdType.MSISDN.getIdType());
        return payer;
    }

    private static RequestPay getRequestPay(MSISDN msisdnObject) {
        return getRequestPay(msisdnObject.getValue());
    }

    private static RequestPay getRequestPay(String msisdnValue) {
        RequestPay requestPay = new RequestPay();
        requestPay.setAmount("6.0");
        requestPay.setCurrency("EUR");
        requestPay.setExternalId(getExternalId());
        requestPay.setPayeeNote("RequestPay payee note");
        requestPay.setPayerMessage("RequestPay payer message");
        requestPay.setPayer(getPayer(msisdnValue));
        return requestPay;
    }

    private static Withdraw getWithdraw(MSISDN msisdnObject) {
        return getWithdraw(msisdnObject.getValue());
    }

    private static Withdraw getWithdraw(String msisdnValue) {
        Withdraw withdraw = new Withdraw();
        withdraw.setAmount("6.0");
        withdraw.setCurrency("EUR");
        withdraw.setExternalId(getExternalId());
        withdraw.setPayeeNote("Withdraw payee note");
        withdraw.setPayerMessage("Withdraw payer message");
        withdraw.setPayer(getPayer(msisdnValue));
        return withdraw;
    }

    private static String getExternalId() {
        String currentTimeMillis = Long.toString(System.currentTimeMillis());
        return currentTimeMillis.substring(currentTimeMillis.length() - 6);
    }
}
