package com.momo.api.integration;

import com.momo.api.base.HttpStatusCode;
import com.momo.api.base.constants.AccessType;
import com.momo.api.base.constants.Constants;
import com.momo.api.base.constants.IdType;
import com.momo.api.base.constants.TargetEnvironment;
import com.momo.api.base.context.collection.CollectionConfiguration;
import com.momo.api.base.context.remittance.RemittanceConfiguration;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.model.BCAuthorize;
import com.momo.api.base.model.StatusResponse;
import com.momo.api.config.MSISDN;
import com.momo.api.config.PropertiesLoader;
import com.momo.api.constants.Environment;
import com.momo.api.constants.NotificationType;
import com.momo.api.models.*;
import com.momo.api.models.collection.RequestPay;
import com.momo.api.requests.collection.CollectionRequest;
import com.momo.api.requests.remittance.RemittanceRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 *
 * Class RemittanceRequestTest
 */
public class RemittanceRequestTest {

    static PropertiesLoader loader;

    @BeforeAll
    public static void init() {
        loader = new PropertiesLoader();
    }

    private final String incorrect_referenceId = "incorrect_referenceId";
    private final static String MSISDN_NUMBER = MSISDN.SUCCESSFUL.getValue();
    private final static String EMAIL = "testmail@gmail.com";
    private final static String SUBSCRIPTION_KEY = "REMITTANCE_SUBSCRIPTION_KEY";
    private final static String COLLECTION_SUBSCRIPTION_KEY = "COLLECTION_SUBSCRIPTION_KEY";
    
    @Test
    @DisplayName("Call Back Url Test Success")
    public void callBackUrlTest() throws MoMoException {
        //Remittance request made without calling "addCallBackUrl(String)" with "remittanceConfiguration" object
        RemittanceConfiguration remittanceConfiguration = new RemittanceConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue());
        RemittanceRequest remittanceRequestFirst = remittanceConfiguration.createRemittanceRequest();
        
        StatusResponse statusResponse;
        
        //case 1: Passing a correct CallBackUrl with the remittanceRequestFirst object. This allows callBack's to be received for all requests made with this "remittanceRequestFirst" object.
        remittanceRequestFirst.addCallBackUrl(loader.get("CALLBACK_URL"));
        statusResponse = remittanceRequestFirst.transfer(getTransfer(MSISDN_NUMBER));
        
        //case 2: Passing NotificationType as "POLLING" with the remittanceRequestFirst object. No callBack's will be received for any requests made with this "remittanceRequestFirst" object unless the addCallBackUrl(String) is set again.
        remittanceRequestFirst.setNotificationType(NotificationType.POLLING);
        statusResponse = remittanceRequestFirst.transfer(getTransfer(MSISDN_NUMBER));
        
        //case 3: Passing a correct CallBackUrl with the remittanceConfiguration object. But will not receive callBack's since the request is made with "remittanceRequestFirst" having a "NotificationType" as "POLLING". To receive callback's again, we need to change "NotificationType" to "CALLBACK" or call the method "addCallBackUrl" with "collectionRequestFirst" with a valid callback url(as show in a later step below).
        remittanceConfiguration.addCallBackUrl(loader.get("CALLBACK_URL"));
        statusResponse = remittanceRequestFirst.transfer(getTransfer(MSISDN_NUMBER));
        
        //case 4: Creating a new remittanceRequestNew object and passing valid callback url. Callback will be received.
        RemittanceRequest remittanceRequestNew = remittanceConfiguration.createRemittanceRequest();
        statusResponse = remittanceRequestNew.transfer(getTransfer(MSISDN_NUMBER));
        
        //case 5: Correcting the CallBackUrl for "remittanceRequestFirst" object will allow callBack's to be received again using this object.
        remittanceRequestFirst.addCallBackUrl(loader.get("CALLBACK_URL"));
        statusResponse = remittanceRequestFirst.transfer(getTransfer(MSISDN_NUMBER));
    }

    @Test
    @DisplayName("Transfer Test Success")
    void transferTestSuccess() throws MoMoException {
        RemittanceConfiguration remittanceConfiguration = new RemittanceConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue()).addCallBackUrl(loader.get("CALLBACK_URL"));
        RemittanceRequest remittanceRequest = remittanceConfiguration.createRemittanceRequest();

        Transfer transfer = getTransfer(MSISDN_NUMBER);

        StatusResponse statusResponse = remittanceRequest.transfer(transfer);
        assertNotNull(statusResponse);
        assertNotNull(statusResponse.getReferenceId());
        assertTrue(statusResponse.getStatus());
    }

    @Test
    @DisplayName("Transfer Test Failure")
    void transferTestFailure() throws MoMoException {
        RemittanceConfiguration remittanceConfiguration = new RemittanceConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue()).addCallBackUrl(loader.get("CALLBACK_URL"));
        RemittanceRequest remittanceRequest = remittanceConfiguration.createRemittanceRequest();

        //case 1: Transfer object is null
        MoMoException moMoException = assertThrows(MoMoException.class, () -> remittanceRequest.transfer(null));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.TRANSFER_OBJECT_INIT_ERROR);

        //case 2: Transfer object initialised, but varialbes are not defined
        Transfer transfer = new Transfer();
        moMoException = assertThrows(MoMoException.class, () -> remittanceRequest.transfer(transfer));
        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.BAD_REQUEST.getHttpStatusCode()));

        //case 3: Payee object initialised, but varialbes are not defined
        transfer.setPayee(new Payee());
        moMoException = assertThrows(MoMoException.class, () -> remittanceRequest.transfer(transfer));
        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.INTERNAL_SERVER_ERROR.getHttpStatusCode()));
    }

    @Test
    @DisplayName("Transfer Status Test Success")
    void transferStatusTestSuccess() throws MoMoException {
        RemittanceConfiguration remittanceConfiguration = new RemittanceConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue()).addCallBackUrl(loader.get("CALLBACK_URL"));
        RemittanceRequest remittanceRequest = remittanceConfiguration.createRemittanceRequest();

        Transfer transfer = getTransfer(MSISDN_NUMBER);

        StatusResponse statusResponse = remittanceRequest.transfer(transfer);

        TransferStatus transferStatus = remittanceRequest.getTransferStatus(statusResponse.getReferenceId());

        assertNotNull(transferStatus);
        assertNotNull(transferStatus.getAmount());
        assertNotNull(transferStatus.getCurrency());
        assertNotNull(transferStatus.getExternalId());
        assertNotNull(transferStatus.getFinancialTransactionId());
        assertNotNull(transferStatus.getPayeeNote());
        assertNotNull(transferStatus.getPayerMessage());
        //assertNotNull(transferStatus.getReason());
        assertNotNull(transferStatus.getStatus());

        assertNotNull(transferStatus.getPayee());
        Payee payer = transferStatus.getPayee();
        assertNotNull(payer.getPartyId());
        assertNotNull(payer.getPartyIdType());
    }

    @Test
    @DisplayName("Transfer Status Test Failure")
    void transferStatusTestFailure() throws MoMoException {
        RemittanceConfiguration remittanceConfiguration = new RemittanceConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue()).addCallBackUrl(loader.get("CALLBACK_URL"));
        RemittanceRequest remittanceRequest = remittanceConfiguration.createRemittanceRequest();

        //case 1: referenceId is null
        MoMoException moMoException = assertThrows(MoMoException.class, () -> remittanceRequest.getTransferStatus(null));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.NULL_VALUE_ERROR);
        
        //case 2: referenceId is empty
        moMoException = assertThrows(MoMoException.class, () -> remittanceRequest.getTransferStatus(""));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.EMPTY_STRING_ERROR);

        //case 3: incorrect referenceId provided
        moMoException = assertThrows(MoMoException.class, () -> remittanceRequest.getTransferStatus(incorrect_referenceId));
        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.BAD_REQUEST.getHttpStatusCode()));
    }
    
    @Test
    @DisplayName("Validate Account Holder Status Test Success")
    void validateAccountHolderStatusTestSuccess() throws MoMoException{
        RemittanceConfiguration remittanceConfiguration = new RemittanceConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue());
        RemittanceRequest remittanceRequest = remittanceConfiguration.createRemittanceRequest();
        
        AccountHolder accountHolder = new AccountHolder(IdType.msisdn.getValue(), MSISDN_NUMBER);
        Result result = remittanceRequest.validateAccountHolderStatus(accountHolder);
        assertNotNull(result);
        assertTrue(result.getResult());
    }
    
    @Test
    @DisplayName("Validate Account Holder Status Test Failure")
    void validateAccountHolderStatusTestFailure() throws MoMoException{
        RemittanceConfiguration remittanceConfiguration = new RemittanceConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue());
        RemittanceRequest remittanceRequest = remittanceConfiguration.createRemittanceRequest();
        
        MoMoException moMoException;
        
        //case 1: AccountHolder is null
        moMoException = assertThrows(MoMoException.class, ()->remittanceRequest.validateAccountHolderStatus(null));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.ACCOUNT_HOLDER_OBJECT_INIT_ERROR);
        
        //case 2: Key or Value is null
        moMoException = assertThrows(MoMoException.class, ()->remittanceRequest.validateAccountHolderStatus(new AccountHolder(IdType.msisdn.getValue(), null)));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.NULL_VALUE_ERROR);

        //case 3: Key or Value is empty
        moMoException = assertThrows(MoMoException.class, ()->remittanceRequest.validateAccountHolderStatus(new AccountHolder("", MSISDN_NUMBER)));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.EMPTY_STRING_ERROR);
    }
    
    @Test
    @DisplayName("Get Account Balance Test Success")
    void getAccountBalanceTestSuccess() throws MoMoException{
        RemittanceConfiguration remittanceConfiguration = new RemittanceConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue());
        RemittanceRequest remittanceRequest = remittanceConfiguration.createRemittanceRequest();
        
        AccountBalance accountBalance = remittanceRequest.getAccountBalance();
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
//    void getAccountBalanceTestFailure() throws MoMoException{
//        //Destroying the existing context to check of requests fail
//        RemittanceContext.destroyContext();
//        //case 1: RemittanceRequest is not properly initialised
//        RemittanceRequest remittanceRequest = new RemittanceRequest();
//        MoMoException moMoException = assertThrows(MoMoException.class, ()->remittanceRequest.getAccountBalance());
//        assertEquals(moMoException.getError().getErrorDescription(), Constants.REQUEST_NOT_CREATED);
//    }
    
    @Test
    @DisplayName("Get Basic Userinfo Test Success")
    void getBasicUserinfoTestSuccess() throws MoMoException{
        RemittanceConfiguration remittanceConfiguration = new RemittanceConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue());
        RemittanceRequest remittanceRequest = remittanceConfiguration.createRemittanceRequest();
        
        BasicUserInfo basicUserInfo = remittanceRequest.getBasicUserinfo(MSISDN_NUMBER);
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
    void getBasicUserinfoTestFailure() throws MoMoException{
        RemittanceConfiguration remittanceConfiguration = new RemittanceConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue());
        RemittanceRequest remittanceRequest = remittanceConfiguration.createRemittanceRequest();
        
        MoMoException moMoException;
        
        //case 1: Value is null
        moMoException = assertThrows(MoMoException.class, ()->remittanceRequest.getBasicUserinfo(null));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.NULL_VALUE_ERROR);

        //case 2: Value is empty
        moMoException = assertThrows(MoMoException.class, ()->remittanceRequest.getBasicUserinfo(""));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.EMPTY_STRING_ERROR);
    }

    @Test
    @DisplayName("Request To Pay Delivery Notification Test Success")
    void requestToPayDeliveryNotificationTestSuccess() throws MoMoException {
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(COLLECTION_SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue()).addCallBackUrl(loader.get("CALLBACK_URL"));
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        RequestPay requestPay = getRequestPay(MSISDN_NUMBER);
        StatusResponse statusResponsePay = collectionRequest.requestToPay(requestPay);

        DeliveryNotification deliveryNotification = new DeliveryNotification();
        deliveryNotification.setNotificationMessage("test message");

        RemittanceConfiguration remittanceConfiguration = new RemittanceConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue()).addCallBackUrl(loader.get("CALLBACK_URL"));
        RemittanceRequest remittanceRequest = remittanceConfiguration.createRemittanceRequest();

        StatusResponse statusResponseDeliveryNotification = remittanceRequest.requestToPayDeliveryNotification(statusResponsePay.getReferenceId(), deliveryNotification);
        assertEquals(statusResponseDeliveryNotification.getStatus(), true);
    }

    @Test
    @DisplayName("Request To Pay Delivery Notification Test Failure")
    void requestToPayDeliveryNotificationTestFailure() throws MoMoException {
        RemittanceConfiguration remittanceConfiguration = new RemittanceConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue()).addCallBackUrl(loader.get("CALLBACK_URL"));
        RemittanceRequest remittanceRequest = remittanceConfiguration.createRemittanceRequest();

        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(COLLECTION_SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue()).addCallBackUrl(loader.get("CALLBACK_URL"));
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();
        RequestPay requestPay = getRequestPay(MSISDN_NUMBER);
        StatusResponse statusResponsePay = collectionRequest.requestToPay(requestPay);
        
        DeliveryNotification deliveryNotification = new DeliveryNotification();
        deliveryNotification.setNotificationMessage("test message");

        //case 1: referenceId given is not from request to pay
        MoMoException moMoException = assertThrows(MoMoException.class, () -> remittanceRequest.requestToPayDeliveryNotification(incorrect_referenceId, deliveryNotification));
        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.BAD_REQUEST.getHttpStatusCode()));
        
        //case 7: Using the same referenceId for requestToPayDeliveryNotification after a successful request was made
        deliveryNotification.setNotificationMessage("test message");
        //making the first request
        StatusResponse statusResponseDeliveryNotification = remittanceRequest.requestToPayDeliveryNotification(statusResponsePay.getReferenceId(), deliveryNotification, "Header Message", "eng");
        assertEquals(statusResponseDeliveryNotification.getStatus(), true);
        //making the second request
        moMoException = assertThrows(MoMoException.class, ()->remittanceRequest.requestToPayDeliveryNotification(statusResponsePay.getReferenceId(), deliveryNotification, "Header Message", "eng"));
        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.TOO_MANY_REQUESTS.getHttpStatusCode()));
    }

    @Test
    @DisplayName("Get Userinfo With Consent Test Success")
    void getUserInfoWithConsentTestSuccess() throws MoMoException {
        RemittanceConfiguration remittanceConfiguration = new RemittanceConfiguration(loader.get(SUBSCRIPTION_KEY),
                loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue());
        RemittanceRequest remittanceRequest = remittanceConfiguration.createRemittanceRequest();

        AccountHolder accountHolderMSISDN = new AccountHolder(IdType.MSISDN.getValue(), MSISDN_NUMBER);
        UserInfo userInfoMSISDN = remittanceRequest.getUserInfoWithConsent(accountHolderMSISDN, "profile", AccessType.OFFLINE);

        assertNotNull(userInfoMSISDN);
        assertNotNull(userInfoMSISDN.getGiven_name());
    }

    @Test
    @DisplayName("Get Userinfo With Consent Test Failure")
    void getUserInfoWithConsentTestFailure() throws MoMoException {
        RemittanceConfiguration remittanceConfiguration = new RemittanceConfiguration(loader.get(SUBSCRIPTION_KEY),
                loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, TargetEnvironment.sandbox.getValue());
        RemittanceRequest remittanceRequest = remittanceConfiguration.createRemittanceRequest();

        AccountHolder accountHolder = new AccountHolder(IdType.MSISDN.getValue(), MSISDN_NUMBER);
        MoMoException moMoException = assertThrows(MoMoException.class, ()->remittanceRequest.getUserInfoWithConsent(accountHolder, "invalid", AccessType.OFFLINE));

        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.INTERNAL_SERVER_ERROR.getHttpStatusCode()));

        AccountHolder accountHolder1 = new AccountHolder(null, null);
        moMoException = assertThrows(MoMoException.class, ()->remittanceRequest.getUserInfoWithConsent(accountHolder1, "profile", AccessType.OFFLINE));

        assertEquals(moMoException.getError().getErrorDescription(), Constants.NULL_VALUE_ERROR);

        AccountHolder accountHolder2 = new AccountHolder(IdType.MSISDN.getValue(), MSISDN_NUMBER);
        moMoException = assertThrows(MoMoException.class, ()->remittanceRequest.getUserInfoWithConsent(accountHolder2, "", AccessType.OFFLINE));

        assertEquals(moMoException.getError().getErrorDescription(), Constants.EMPTY_STRING_ERROR);

    }


    private static Payee getPayee(String msisdnValue) {
        Payee payee = new Payee();
        payee.setPartyId(msisdnValue);
        payee.setPartyIdType(IdType.MSISDN.getValue());
        return payee;
    }

    private static Transfer getTransfer(MSISDN msisdnObject) {
        return getTransfer(msisdnObject.getValue());
    }

    private static Transfer getTransfer(String msisdnValue) {
        Transfer transfer = new Transfer();
        transfer.setAmount("6.0");
        transfer.setCurrency("EUR");
        transfer.setExternalId("6353636");
        transfer.setPayeeNote("Transfer payee note");
        transfer.setPayerMessage("Transfer payer message");
        transfer.setPayee(getPayee(msisdnValue));
        return transfer;
    }

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
        requestPay.setExternalId("6353636");
        requestPay.setPayeeNote("RequestPay payee note");
        requestPay.setPayerMessage("RequestPay payer message");
        requestPay.setPayer(getPayer(msisdnValue));
        return requestPay;
    }
}
