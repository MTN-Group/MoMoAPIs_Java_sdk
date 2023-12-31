package com.momo.api.integration;

import com.momo.api.base.HttpStatusCode;
import com.momo.api.base.constants.AccessType;
import com.momo.api.base.constants.Constants;
import com.momo.api.base.constants.IdType;
import com.momo.api.base.constants.TargetEnvironment;
import com.momo.api.base.context.collection.CollectionConfiguration;
import com.momo.api.base.context.provisioning.UserProvisioningConfiguration;
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
import com.momo.api.models.UserInfo;
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

    static PropertiesLoader loader;

    @BeforeAll
    public static void init() {
        loader = new PropertiesLoader();
    }

    private final String incorrect_referenceId = "incorrect_referenceId";
    private final static String MSISDN_NUMBER = MSISDN.SUCCESSFUL.getValue();
    private final static String EMAIL = "testmail@gmail.com";
    private final static String SUBSCRIPTION_KEY = "COLLECTION_SUBSCRIPTION_KEY";

    @Test
    @DisplayName("Call Back Url Test")
    public void callBackUrlTest() throws MoMoException {
        //Collection request made without calling "addCallBackUrl(String)" with "CollectionConfiguration" object
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue());
        CollectionRequest collectionRequestFirst = collectionConfiguration.createCollectionRequest();

        StatusResponse statusResponse;

        //case 1: Passing a correct CallBackUrl with the collectionRequestFirst object. This allows callBack's to be received for all requests made with this "collectionRequestFirst" object.
        collectionRequestFirst.addCallBackUrl(loader.get("CALLBACK_URL"));
        statusResponse = collectionRequestFirst.requestToPay(getRequestPay(MSISDN_NUMBER+"1"));
        assertTrue(statusResponse.getStatus());

        //case 2: Passing NotificationType as "POLLING" with the collectionRequestFirst object. No callBack's will be received for any requests made with this "collectionRequestFirst" object unless the addCallBackUrl(String) is set again.
        collectionRequestFirst.setNotificationType(NotificationType.POLLING);
        statusResponse = collectionRequestFirst.requestToPay(getRequestPay(MSISDN_NUMBER+"2"));
        assertTrue(statusResponse.getStatus());

        //case 3: Passing a correct CallBackUrl with the collectionConfiguration object. But will not receive callBack's since the request is made with "collectionRequestFirst" having a "NotificationType" as "POLLING". To receive callback's again, we need to change "NotificationType" to "CALLBACK" or call the method "addCallBackUrl" with "collectionRequestFirst" with a valid callback url(as show in a later step below).
        collectionConfiguration.addCallBackUrl(loader.get("CALLBACK_URL"));
        statusResponse = collectionRequestFirst.requestToPay(getRequestPay(MSISDN_NUMBER+"3"));
        assertTrue(statusResponse.getStatus());

        //case 4: Creating a new collectionRequestSecond object and passing valid callback url. Callback will be received.
        CollectionRequest collectionRequestSecond = collectionConfiguration.createCollectionRequest();
        statusResponse = collectionRequestSecond.requestToPay(getRequestPay(MSISDN_NUMBER+"4"));
        assertTrue(statusResponse.getStatus());

        //case 5: Correcting the CallBackUrl for "collectionRequestFirst" object will allow callBack's to be received again using this object.
        statusResponse = collectionRequestFirst
                .addCallBackUrl(loader.get("CALLBACK_URL"))
                .requestToPay(getRequestPay(MSISDN_NUMBER+"5"));
//        statusResponse = collectionRequestFirst.requestToPay(getRequestPay(MSISDN_NUMBER+"5"));
        assertTrue(statusResponse.getStatus());
    }

    @Test
    @DisplayName("Request To Pay Test Success")
    void requestToPayTestSuccess() throws MoMoException {
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue()).addCallBackUrl(loader.get("CALLBACK_URL"));
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
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue()).addCallBackUrl(loader.get("CALLBACK_URL"));
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        //case 1: Pay object is null
        MoMoException moMoException = assertThrows(MoMoException.class, () -> collectionRequest.requestToPay(null));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.REQUEST_TO_PAY_OBJECT_INIT_ERROR);

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
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue()).addCallBackUrl(loader.get("CALLBACK_URL"));
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
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue()).addCallBackUrl(loader.get("CALLBACK_URL"));
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
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue());
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        AccountBalance accountBalance = collectionRequest.getAccountBalance();
        assertNotNull(accountBalance);
        assertNotNull(accountBalance.getAvailableBalance());
        assertNotNull(accountBalance.getCurrency());
    }

//    /**
//     * Making an API request without creating context
//     *
//     * @throws MoMoException
//     */
//    @Test
//    @DisplayName("Get Account Balance Test Failure")
//    void getAccountBalanceTestFailure() throws MoMoException {
//        //Destroying the existing context to check of requests fail
//        CollectionConfiguration.destroySingletonObject();
//        //case 1: CollectionRequest is not properly initialised
//        CollectionRequest collectionRequest = new CollectionRequest();
//        MoMoException moMoException = assertThrows(MoMoException.class, () -> collectionRequest.getAccountBalance());
//        assertEquals(moMoException.getError().getErrorDescription(), Constants.REQUEST_NOT_CREATED);
//    }

    @Test
    @DisplayName("Get Account Balance In Specific Currency Test Success")
    void getAccountBalanceInSpecificCurrencyTestSuccess() throws MoMoException {
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue());
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        AccountBalance accountBalance = collectionRequest.getAccountBalanceInSpecificCurrency("EUR");
        assertNotNull(accountBalance);
        assertNotNull(accountBalance.getAvailableBalance());
        assertNotNull(accountBalance.getCurrency());
    }

    @Test
    @DisplayName("Get Account Balance In Specific Currency Test Failure")
    void getAccountBalanceInSpecificCurrencyTestFailure() throws MoMoException {
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue());
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

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
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue()).addCallBackUrl(loader.get("CALLBACK_URL"));
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        RequestPay requestPay = getRequestPay(MSISDN_NUMBER);
        StatusResponse statusResponsePay = collectionRequest.requestToPay(requestPay);

        DeliveryNotification deliveryNotification = new DeliveryNotification();
        deliveryNotification.setNotificationMessage("test message");

        StatusResponse statusResponseDeliveryNotification = collectionRequest.requestToPayDeliveryNotification(statusResponsePay.getReferenceId(), deliveryNotification);
        assertEquals(statusResponseDeliveryNotification.getStatus(), true);

        statusResponsePay = collectionRequest.requestToPay(requestPay);

        statusResponseDeliveryNotification = collectionRequest.requestToPayDeliveryNotification(statusResponsePay.getReferenceId(), deliveryNotification, "Header Message", "eng");
        assertEquals(statusResponseDeliveryNotification.getStatus(), true);
    }

    @Test
    @DisplayName("Request To Pay Delivery Notification Test Failure")
    void requestToPayDeliveryNotificationTestFailure() throws MoMoException {
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue()).addCallBackUrl(loader.get("CALLBACK_URL"));
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

        String stringLength_161 = "01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";

        //case 5: NotificationMessage string in DeliveryNotification object is more than 160 characters
        deliveryNotification.setNotificationMessage(stringLength_161);
        moMoException = assertThrows(MoMoException.class, () -> collectionRequest.requestToPayDeliveryNotification(statusResponsePay.getReferenceId(), deliveryNotification));
        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.BAD_REQUEST.getHttpStatusCode()));

        //case 6: Using the same referenceId for requestToPayDeliveryNotification after a successful request was made
        deliveryNotification.setNotificationMessage("test message");
        //making the first request
        StatusResponse statusResponseDeliveryNotification = collectionRequest.requestToPayDeliveryNotification(statusResponsePay.getReferenceId(), deliveryNotification, "Header Message", "eng");
        assertEquals(statusResponseDeliveryNotification.getStatus(), true);
        //making the second request
        moMoException = assertThrows(MoMoException.class, () -> collectionRequest.requestToPayDeliveryNotification(statusResponsePay.getReferenceId(), deliveryNotification, "Header Message", "eng"));
        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.TOO_MANY_REQUESTS.getHttpStatusCode()));

    }

    @Test
    @DisplayName("Validate Account Holder Status Test Success")
    void validateAccountHolderStatusTestSuccess() throws MoMoException {
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue());
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        AccountHolder accountHolder = new AccountHolder(IdType.msisdn.getValue(), MSISDN_NUMBER);
        Result result = collectionRequest.validateAccountHolderStatus(accountHolder);
        assertNotNull(result);
        assertTrue(result.getResult());
    }

    @Test
    @DisplayName("Validate Account Holder Status Test Failure")
    void validateAccountHolderStatusTestFailure() throws MoMoException {
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue());
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        MoMoException moMoException;

        //case 1: AccountHolder is null
        moMoException = assertThrows(MoMoException.class, () -> collectionRequest.validateAccountHolderStatus(null));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.ACCOUNT_HOLDER_OBJECT_INIT_ERROR);

        //case 2: Key or Value is null
        moMoException = assertThrows(MoMoException.class, () -> collectionRequest.validateAccountHolderStatus(new AccountHolder(IdType.msisdn.getValue(), null)));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.NULL_VALUE_ERROR);

        //case 3: Key or Value is empty
        moMoException = assertThrows(MoMoException.class, () -> collectionRequest.validateAccountHolderStatus(new AccountHolder("", MSISDN_NUMBER)));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.EMPTY_STRING_ERROR);
    }

    @Test
    @DisplayName("Request To Withdraw V1 Test Success")
    void requestToWithdrawV1TestSuccess() throws MoMoException {
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue()).addCallBackUrl(loader.get("CALLBACK_URL"));
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
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue()).addCallBackUrl(loader.get("CALLBACK_URL"));
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
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue()).addCallBackUrl(loader.get("CALLBACK_URL"));
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
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue()).addCallBackUrl(loader.get("CALLBACK_URL"));
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
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue()).addCallBackUrl(loader.get("CALLBACK_URL"));
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
        //assertNotNull(payStatus.getReason());
        assertNotNull(withdrawStatus.getStatus());

        assertNotNull(withdrawStatus.getPayer());
        Payer payer = withdrawStatus.getPayer();
        assertNotNull(payer.getPartyId());
        assertNotNull(payer.getPartyIdType());
    }

    @Test
    @DisplayName("Request To Withdraw Transaction Status Test Failure")
    void requestToWithdrawTransactionStatusTestFailure() throws MoMoException {
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue()).addCallBackUrl(loader.get("CALLBACK_URL"));
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
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue());
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
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue());
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
    @DisplayName("Get Userinfo With Consent Test Success")
    void getUserInfoWithConsentTestSuccess() throws MoMoException {
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue());
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        AccountHolder accountHolderMSISDN = new AccountHolder(IdType.MSISDN.getValue(), MSISDN_NUMBER);
        UserInfo userInfoMSISDN = collectionRequest.getUserInfoWithConsent(accountHolderMSISDN, "profile", AccessType.OFFLINE);
        
        assertNotNull(userInfoMSISDN);
        assertNotNull(userInfoMSISDN.getGiven_name());
        
        AccountHolder accountHolderEMAIL = new AccountHolder(IdType.EMAIL.getValue(), EMAIL);
        UserInfo userInfoEMAIL = collectionRequest.getUserInfoWithConsent(accountHolderEMAIL, "profile", AccessType.OFFLINE);
        
        assertNotNull(userInfoEMAIL);
        assertNotNull(userInfoEMAIL.getGiven_name());
        
//        AccountHolder accountHolderUUID = new AccountHolder(IdType.PARTY_CODE.getValue(), loader.get("REFERENCE_ID"));
//        UserInfo userInfoUUID = collectionRequest.getUserInfoWithConsent(accountHolderUUID, "profile", AccessType.OFFLINE);
//        
//        assertNotNull(userInfoUUID);
//        assertNotNull(userInfoUUID.getGiven_name());
    }
    
    @Test
    @DisplayName("Get Userinfo With Consent Test Failure")
    void getUserInfoWithConsentTestFailure() throws MoMoException {
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue());
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        AccountHolder accountHolder = new AccountHolder(IdType.MSISDN.getValue(), MSISDN_NUMBER);
        MoMoException moMoException = assertThrows(MoMoException.class, ()->collectionRequest.getUserInfoWithConsent(accountHolder, "invalid", AccessType.OFFLINE));
        
        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.INTERNAL_SERVER_ERROR.getHttpStatusCode()));

        AccountHolder accountHolder1 = new AccountHolder(null, null);
        moMoException = assertThrows(MoMoException.class, ()->collectionRequest.getUserInfoWithConsent(accountHolder1, "profile", AccessType.OFFLINE));
        
        assertEquals(moMoException.getError().getErrorDescription(), Constants.NULL_VALUE_ERROR);
        
        AccountHolder accountHolder2 = new AccountHolder(IdType.MSISDN.getValue(), MSISDN_NUMBER);
        moMoException = assertThrows(MoMoException.class, ()->collectionRequest.getUserInfoWithConsent(accountHolder2, "", AccessType.OFFLINE));
        
        assertEquals(moMoException.getError().getErrorDescription(), Constants.EMPTY_STRING_ERROR);
        
        AccountHolder accountHolder3 = new AccountHolder(IdType.MSISDN.getValue(), MSISDN_NUMBER);
        moMoException = assertThrows(MoMoException.class, ()->collectionRequest.getUserInfoWithConsent(accountHolder3, "profile", null));
        
        assertEquals(moMoException.getError().getErrorDescription(), Constants.NULL_VALUE_ERROR);
        
        moMoException = assertThrows(MoMoException.class, ()->collectionRequest.getUserInfoWithConsent(null, "profile", AccessType.OFFLINE));
        
        assertEquals(moMoException.getError().getErrorDescription(), Constants.NULL_VALUE_ERROR);
    }

    @Test
    @DisplayName("Callback Host Test Success")
    void callbackHostTestSuccess() throws MoMoException {
        CallbackHost callbackHost = new CallbackHost("webhook.site");
        UserProvisioningConfiguration userProvisioningConfiguration = new UserProvisioningConfiguration(loader.get(SUBSCRIPTION_KEY));
        UserProvisioningRequest userProvisioningRequest = userProvisioningConfiguration.createUserProvisioningRequest();

        StatusResponse statusResponse = userProvisioningRequest.createUser(callbackHost);

        ApiKey apiKey = userProvisioningRequest.createApiKey(statusResponse.getReferenceId());

        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), statusResponse.getReferenceId(), apiKey.getApiKey(), Environment.SANDBOX, TargetEnvironment.sandbox.getValue()).addCallBackUrl(loader.get("CALLBACK_URL"));
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        RequestPay requestPay = getRequestPay(MSISDN_NUMBER);

        statusResponse = collectionRequest.requestToPay(requestPay);
        assertNotNull(statusResponse);
        assertNotNull(statusResponse.getReferenceId());
        assertEquals(UUID.fromString(statusResponse.getReferenceId()).toString(), statusResponse.getReferenceId());
        assertTrue(statusResponse.getStatus());
    }

    @Test
    @DisplayName("Request To Pay With EMAIL Test Success")
    void requestToPayWithEMAILTestSuccess() throws MoMoException {
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue()).addCallBackUrl(loader.get("CALLBACK_URL"));
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        Payer payer = new Payer();
        payer.setPartyId(EMAIL);
        payer.setPartyIdType(IdType.EMAIL.getValue());
        
        RequestPay requestPay = new RequestPay();
        requestPay.setAmount("6.0");
        requestPay.setCurrency("EUR");
        requestPay.setExternalId(getExternalId());
        requestPay.setPayeeNote("RequestPay payee note");
        requestPay.setPayerMessage("RequestPay payer message");
        requestPay.setPayer(payer);

        StatusResponse statusResponse = collectionRequest.requestToPay(requestPay);
        assertNotNull(statusResponse);
        assertNotNull(statusResponse.getReferenceId());
        assertEquals(UUID.fromString(statusResponse.getReferenceId()).toString(), statusResponse.getReferenceId());
        assertTrue(statusResponse.getStatus());
    }

    @Test
    @DisplayName("Request To Pay With PARTY_CODE Test Success")
    void requestToPayWithPARTY_CODETestSuccess() throws MoMoException {
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue()).addCallBackUrl(loader.get("CALLBACK_URL"));
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        Payer payer = new Payer();
        payer.setPartyId(UUID.randomUUID().toString());
        payer.setPartyIdType(IdType.PARTY_CODE.getValue());
        
        RequestPay requestPay = new RequestPay();
        requestPay.setAmount("6.0");
        requestPay.setCurrency("EUR");
        requestPay.setExternalId(getExternalId());
        requestPay.setPayeeNote("RequestPay payee note");
        requestPay.setPayerMessage("RequestPay payer message");
        requestPay.setPayer(payer);

        StatusResponse statusResponse = collectionRequest.requestToPay(requestPay);
        assertNotNull(statusResponse);
        assertNotNull(statusResponse.getReferenceId());
        assertEquals(UUID.fromString(statusResponse.getReferenceId()).toString(), statusResponse.getReferenceId());
        assertTrue(statusResponse.getStatus());
    }

//    /**
//     * Checking if providing a callBackUrl with different host throws error 
//     *
//     * @throws MoMoException
//     */
//    @Test
//    @DisplayName("Callback Host Test Failure")
//    void callbackHostTestFailure() throws MoMoException {
//        //destroying the exising valid UserProvisioning singleton instance, to create new instance with invalid "CallbackHost"
//        UserProvisioningContext.destroyContext();
//        //CallbackHost is "something.site", so the "callBackUrl" must be a url with "something.site" as its host address
//        CallbackHost callbackHost = new CallbackHost("something.site");
//        UserProvisioningConfiguration userProvisioningConfiguration = new UserProvisioningConfiguration(loader.get(SUBSCRIPTION_KEY));
//        UserProvisioningRequest userProvisioningRequest = userProvisioningConfiguration.createUserProvisioningRequest();
//
//        StatusResponse statusResponse = userProvisioningRequest.createUser(callbackHost);
//
//        ApiKey apiKey = userProvisioningRequest.createApiKey(statusResponse.getReferenceId());
//        //destroying the newly created singleton instance having invalid "CallbackHost" value. Now a new instance can be created
//        UserProvisioningContext.destroyContext();
//
//        //destroying the exising collection singleton instance, to create new instance with the above parameters 
//        CollectionConfiguration.destroyContext();
//        //here 'loader.get("CALLBACK_URL")' need to contain a callBackUrl with a host address different than "something.site", so that it throws error
//        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(SUBSCRIPTION_KEY), statusResponse.getReferenceId(), apiKey.getApiKey(), Environment.SANDBOX, TargetEnvironment.sandbox.getValue()).addCallBackUrl(loader.get("CALLBACK_URL"));
//        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();
//
//        RequestPay requestPay = getRequestPay(MSISDN_NUMBER);
//
//        MoMoException moMoException = assertThrows(MoMoException.class, () -> collectionRequest.requestToPay(requestPay));
//        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.INTERNAL_SERVER_ERROR.getHttpStatusCode()));
//        //destroying the newly created collection singleton instance. Now a new instance can be created
//        CollectionConfiguration.destroyContext();
//    }

    private static Payer getPayer(String msisdnValue) {
        Payer payer = new Payer();
        payer.setPartyId(msisdnValue);
        payer.setPartyIdType(IdType.MSISDN.getValue());
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
