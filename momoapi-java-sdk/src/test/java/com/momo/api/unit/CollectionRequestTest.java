package com.momo.api.unit;

import com.momo.api.base.constants.AccessType;
import com.momo.api.base.constants.Constants;
import com.momo.api.base.constants.IdType;
import com.momo.api.base.constants.TargetEnvironment;
import com.momo.api.base.context.collection.CollectionConfiguration;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.model.BCAuthorize;
import com.momo.api.base.model.StatusResponse;
import com.momo.api.constants.Environment;
import com.momo.api.models.AccountBalance;
import com.momo.api.models.AccountHolder;
import com.momo.api.models.BasicUserInfo;
import com.momo.api.models.DeliveryNotification;
import com.momo.api.models.Payer;
import com.momo.api.models.Result;
import com.momo.api.models.UserInfo;
import com.momo.api.models.collection.RequestPayStatus;
import com.momo.api.models.collection.RequestPay;
import com.momo.api.models.collection.Withdraw;
import com.momo.api.models.collection.WithdrawStatus;
import com.momo.api.requests.collection.CollectionRequest;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

/**
 *
 * Class CollectionRequestTest
 */
public class CollectionRequestTest {

    private static final String REFERENCE_ID_RETURNED = UUID.randomUUID().toString();
    private static final String REFERENCE_ID_PARAMETER = UUID.randomUUID().toString();
    private static final String MSISDN = "23423423450";

    @Test
    @DisplayName("Collection Configuration Test Success")
    void collectionConfigurationTestFailure() throws MoMoException {
        MoMoException moMoException = assertThrows(MoMoException.class, () -> new CollectionConfiguration(
                "",
                "REFERENCE_ID",
                "API_KEY",
                Environment.SANDBOX,
                TargetEnvironment.sandbox.getValue()));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.EMPTY_STRING_ERROR);
        moMoException = assertThrows(MoMoException.class, () -> new CollectionConfiguration(
                "SUBSCRIPTION_KEY",
                null,
                "API_KEY",
                Environment.SANDBOX,
                TargetEnvironment.sandbox.getValue()));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.NULL_VALUE_ERROR);
        moMoException = assertThrows(MoMoException.class, () -> new CollectionConfiguration(
                "SUBSCRIPTION_KEY",
                "REFERENCE_ID",
                "API_KEY",
                null,
                TargetEnvironment.sandbox.getValue()));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.NULL_VALUE_ERROR);
    }

    @Test
    @DisplayName("Request To Pay Test Success")
    void requestToPayTestSuccess() throws MoMoException {
        CollectionRequest collectionRequestSpy = spy(new CollectionRequest());

        RequestPay requestPay = getRequestPay();
        StatusResponse expectedStatusResponse = getExpectedStatusResponse(true);
        doReturn(expectedStatusResponse).when(collectionRequestSpy).requestToPay(requestPay);

        StatusResponse statusResponse = collectionRequestSpy.requestToPay(requestPay);
        assertNotNull(statusResponse);
        assertNotNull(statusResponse.getReferenceId());
        assertEquals(statusResponse.getReferenceId(), expectedStatusResponse.getReferenceId());
    }

    @Test
    @DisplayName("Request To Pay Test Failure")
    void requestToPayTestFailure() throws MoMoException {
        CollectionRequest collectionRequestSpy = spy(new CollectionRequest());

        RequestPay requestPay = getRequestPay();
        doThrow(MoMoException.class).when(collectionRequestSpy).requestToPay(requestPay);

        assertThrows(MoMoException.class, () -> collectionRequestSpy.requestToPay(requestPay));
    }

    @Test
    @DisplayName("Request To Pay Transaction Status Test Success")
    void requestToPayTransactionStatusTestSuccess() throws MoMoException {
        CollectionRequest collectionRequestSpy = spy(new CollectionRequest());

        RequestPayStatus expectedRequestPayStatus = getExpectedPayStatus();
        doReturn(expectedRequestPayStatus).when(collectionRequestSpy).requestToPayTransactionStatus(REFERENCE_ID_PARAMETER);

        RequestPayStatus actualRequestPayStatus = collectionRequestSpy.requestToPayTransactionStatus(REFERENCE_ID_PARAMETER);
        assertNotNull(actualRequestPayStatus);
        assertNotNull(actualRequestPayStatus.getAmount());
        assertEquals(actualRequestPayStatus.getAmount(), expectedRequestPayStatus.getAmount());
    }

    @Test
    @DisplayName("Request To Pay Transaction Status Test Failure")
    void requestToPayTransactionStatusTestFailure() throws MoMoException {
        CollectionRequest collectionRequestSpy = spy(new CollectionRequest());

        doThrow(MoMoException.class).when(collectionRequestSpy).requestToPayTransactionStatus(REFERENCE_ID_PARAMETER);

        assertThrows(MoMoException.class, () -> collectionRequestSpy.requestToPayTransactionStatus(REFERENCE_ID_PARAMETER));
    }

    @Test
    @DisplayName("Get Account Balance Test Success")
    void getAccountBalanceTestSuccess() throws MoMoException {
        CollectionRequest collectionRequestSpy = spy(new CollectionRequest());

        AccountBalance expectedAccountBalance = getExpectedAccountBalance();
        doReturn(expectedAccountBalance).when(collectionRequestSpy).getAccountBalance();

        AccountBalance actualAccountBalance = collectionRequestSpy.getAccountBalance();
        assertNotNull(actualAccountBalance);
        assertNotNull(actualAccountBalance.getAvailableBalance());
        assertEquals(actualAccountBalance.getAvailableBalance(), expectedAccountBalance.getAvailableBalance());
    }

    @Test
    @DisplayName("Get Account Balance Test Failure")
    void getAccountBalanceTestFailure() throws MoMoException {
        CollectionRequest collectionRequestSpy = spy(new CollectionRequest());

        doThrow(MoMoException.class).when(collectionRequestSpy).getAccountBalance();

        assertThrows(MoMoException.class, () -> collectionRequestSpy.getAccountBalance());
    }

    @Test
    @DisplayName("Get Account Balance In Specific Currency Test Success")
    void getAccountBalanceInSpecificCurrencyTestSuccess() throws MoMoException {
        CollectionRequest collectionRequestSpy = spy(new CollectionRequest());

        AccountBalance expectedAccountBalance = getExpectedAccountBalance();
        doReturn(expectedAccountBalance).when(collectionRequestSpy).getAccountBalanceInSpecificCurrency("EUR");

        AccountBalance actualAccountBalance = collectionRequestSpy.getAccountBalanceInSpecificCurrency("EUR");
        assertNotNull(actualAccountBalance);
        assertNotNull(actualAccountBalance.getAvailableBalance());
        assertEquals(actualAccountBalance.getAvailableBalance(), expectedAccountBalance.getAvailableBalance());
    }

    @Test
    @DisplayName("Get Account Balance In Specific Currency Test Failure")
    void getAccountBalanceInSpecificCurrencyTestFailure() throws MoMoException {
        CollectionRequest collectionRequestSpy = spy(new CollectionRequest());

        doThrow(MoMoException.class).when(collectionRequestSpy).getAccountBalanceInSpecificCurrency("EUR");

        assertThrows(MoMoException.class, () -> collectionRequestSpy.getAccountBalanceInSpecificCurrency("EUR"));
    }

    @Test
    @DisplayName("Request To Pay Delivery Notification Test Success")
    void requestToPayDeliveryNotificationTestSuccess() throws MoMoException {
        CollectionRequest collectionRequestSpy = spy(new CollectionRequest());

        DeliveryNotification deliveryNotification = new DeliveryNotification();
        deliveryNotification.setNotificationMessage("test message");

        StatusResponse expectedStatusResponse = getExpectedStatusResponse(false);
        doReturn(expectedStatusResponse).when(collectionRequestSpy).requestToPayDeliveryNotification(REFERENCE_ID_PARAMETER, deliveryNotification, "Header Message", "eng");

        StatusResponse statusResponse = collectionRequestSpy.requestToPayDeliveryNotification(REFERENCE_ID_PARAMETER, deliveryNotification, "Header Message", "eng");
        assertNotNull(statusResponse);
        assertNotNull(statusResponse.getStatus());
        assertEquals(statusResponse.getReferenceId(), null);
        assertEquals(statusResponse.getStatus(), expectedStatusResponse.getStatus());
    }

    @Test
    @DisplayName("Request To Pay Delivery Notification Test Failure")
    void requestToPayDeliveryNotificationTestFailure() throws MoMoException {
        CollectionRequest collectionRequestSpy = spy(new CollectionRequest());

        DeliveryNotification deliveryNotification = new DeliveryNotification();
        doThrow(MoMoException.class).when(collectionRequestSpy).requestToPayDeliveryNotification(REFERENCE_ID_PARAMETER, deliveryNotification, "Header Message", "eng");

        assertThrows(MoMoException.class, () -> collectionRequestSpy.requestToPayDeliveryNotification(REFERENCE_ID_PARAMETER, deliveryNotification, "Header Message", "eng"));
    }

    @Test
    @DisplayName("Validate Account Holder Status Test Success")
    void validateAccountHolderStatusTestSuccess() throws MoMoException {
        CollectionRequest collectionRequestSpy = spy(new CollectionRequest());

        Result expectedResult = getExpectedResult();
        AccountHolder accountHolder = new AccountHolder(IdType.msisdn.getValue(), MSISDN);
        doReturn(expectedResult).when(collectionRequestSpy).validateAccountHolderStatus(accountHolder);

        Result actualResult = collectionRequestSpy.validateAccountHolderStatus(accountHolder);
        assertNotNull(actualResult);
        assertEquals(actualResult.getResult(), expectedResult.getResult());
    }

    @Test
    @DisplayName("Validate Account Holder Status Test Failure")
    void validateAccountHolderStatusTestFailure() throws MoMoException {
        CollectionRequest collectionRequestSpy = spy(new CollectionRequest());

        AccountHolder accountHolder = new AccountHolder(IdType.msisdn.getValue(), MSISDN);
        doThrow(MoMoException.class).when(collectionRequestSpy).validateAccountHolderStatus(accountHolder);

        assertThrows(MoMoException.class, () -> collectionRequestSpy.validateAccountHolderStatus(accountHolder));
    }

    @Test
    @DisplayName("Request To Withdraw V1 Test Success")
    void requestToWithdrawV1TestSuccess() throws MoMoException {
        CollectionRequest collectionRequestSpy = spy(new CollectionRequest());

        Withdraw withdraw = getWithdraw();
        StatusResponse expectedStatusResponse = getExpectedStatusResponse(true);
        doReturn(expectedStatusResponse).when(collectionRequestSpy).requestToWithdrawV1(withdraw);

        StatusResponse statusResponse = collectionRequestSpy.requestToWithdrawV1(withdraw);
        assertNotNull(statusResponse);
        assertNotNull(statusResponse.getReferenceId());
        assertEquals(statusResponse.getReferenceId(), expectedStatusResponse.getReferenceId());
    }

    @Test
    @DisplayName("Request To Withdraw V1 Test Failure")
    void requestToWithdrawV1TestFailure() throws MoMoException {
        CollectionRequest collectionRequestSpy = spy(new CollectionRequest());

        Withdraw withdraw = getWithdraw();
        doThrow(MoMoException.class).when(collectionRequestSpy).requestToWithdrawV1(withdraw);

        assertThrows(MoMoException.class, () -> collectionRequestSpy.requestToWithdrawV1(withdraw));
    }

    @Test
    @DisplayName("Request To Withdraw V2 Test Success")
    void requestToWithdrawV2TestSuccess() throws MoMoException {
        CollectionRequest collectionRequestSpy = spy(new CollectionRequest());

        Withdraw withdraw = getWithdraw();
        StatusResponse expectedStatusResponse = getExpectedStatusResponse(true);
        doReturn(expectedStatusResponse).when(collectionRequestSpy).requestToWithdrawV2(withdraw);

        StatusResponse statusResponse = collectionRequestSpy.requestToWithdrawV2(withdraw);
        assertNotNull(statusResponse);
        assertNotNull(statusResponse.getReferenceId());
        assertEquals(statusResponse.getReferenceId(), expectedStatusResponse.getReferenceId());
    }

    @Test
    @DisplayName("Request To Withdraw V2 Test Failure")
    void requestToWithdrawV2TestFailure() throws MoMoException {
        CollectionRequest collectionRequestSpy = spy(new CollectionRequest());

        Withdraw withdraw = getWithdraw();
        doThrow(MoMoException.class).when(collectionRequestSpy).requestToWithdrawV2(withdraw);

        assertThrows(MoMoException.class, () -> collectionRequestSpy.requestToWithdrawV2(withdraw));
    }

    @Test
    @DisplayName("Request To Withdraw Transaction Status Test Success")
    void requestToWithdrawTransactionStatusTestSuccess() throws MoMoException {
        CollectionRequest collectionRequestSpy = spy(new CollectionRequest());

        WithdrawStatus expectedWithdrawStatus = getExpectedWithdrawStatus();
        doReturn(expectedWithdrawStatus).when(collectionRequestSpy).requestToWithdrawTransactionStatus(REFERENCE_ID_PARAMETER);

        WithdrawStatus actualWithdrawStatus = collectionRequestSpy.requestToWithdrawTransactionStatus(REFERENCE_ID_PARAMETER);
        assertNotNull(actualWithdrawStatus);
        assertNotNull(actualWithdrawStatus.getAmount());
        assertEquals(actualWithdrawStatus.getAmount(), expectedWithdrawStatus.getAmount());
    }

    @Test
    @DisplayName("Request To Withdraw Transaction Status Test Failure")
    void requestToWithdrawTransactionStatusTestFailure() throws MoMoException {
        CollectionRequest collectionRequestSpy = spy(new CollectionRequest());

        doThrow(MoMoException.class).when(collectionRequestSpy).requestToWithdrawTransactionStatus(REFERENCE_ID_PARAMETER);

        assertThrows(MoMoException.class, () -> collectionRequestSpy.requestToWithdrawTransactionStatus(REFERENCE_ID_PARAMETER));
    }

    @Test
    @DisplayName("Get Basic Userinfo Test Success")
    void getBasicUserinfoTestSuccess() throws MoMoException {
        CollectionRequest collectionRequestSpy = spy(new CollectionRequest());

        BasicUserInfo expectedBasicUserInfo = getExpectedBasicUserInfo();
        doReturn(expectedBasicUserInfo).when(collectionRequestSpy).getBasicUserinfo(MSISDN);

        BasicUserInfo basicUserInfo = collectionRequestSpy.getBasicUserinfo(MSISDN);
        assertNotNull(basicUserInfo);
        assertNotNull(basicUserInfo.getBirthdate());
        assertEquals(basicUserInfo.getBirthdate(), expectedBasicUserInfo.getBirthdate());
    }

    @Test
    @DisplayName("Get Basic Userinfo Test Failure")
    void getBasicUserinfoTestFailure() throws MoMoException {
        CollectionRequest collectionRequestSpy = spy(new CollectionRequest());

        doThrow(MoMoException.class).when(collectionRequestSpy).getBasicUserinfo(MSISDN);

        assertThrows(MoMoException.class, () -> collectionRequestSpy.getBasicUserinfo(MSISDN));
    }
    
    @Test
    @DisplayName("Get Userinfo With Consent Test Success")
    void getUserInfoWithConsentTestSuccess() throws MoMoException {
        CollectionRequest collectionRequestSpy = spy(new CollectionRequest());
        
        AccountHolder accountHolder = new AccountHolder(IdType.MSISDN.getValue(), MSISDN);
        UserInfo expectedUserInfo = getExpectedUserInfo();
        doReturn(expectedUserInfo).when(collectionRequestSpy).getUserInfoWithConsent(accountHolder, "profile", AccessType.OFFLINE);
        
        UserInfo userInfo = collectionRequestSpy.getUserInfoWithConsent(accountHolder, "profile", AccessType.OFFLINE);
        
        assertEquals(userInfo.getName(), expectedUserInfo.getName());
    }

    @Test
    @DisplayName("Get Userinfo With Consent Test Failure")
    void getUserInfoWithConsentTestFailure() throws MoMoException {
        CollectionRequest collectionRequestSpy = spy(new CollectionRequest());
        
        AccountHolder accountHolder = new AccountHolder(IdType.MSISDN.getValue(), MSISDN);
        doThrow(MoMoException.class).when(collectionRequestSpy).getUserInfoWithConsent(accountHolder, "profile", AccessType.OFFLINE);

        assertThrows(MoMoException.class, () -> collectionRequestSpy.getUserInfoWithConsent(accountHolder, "profile", AccessType.OFFLINE));
    }
    
    private StatusResponse getExpectedStatusResponse(boolean haveReferenceId) {
        StatusResponse statusResponse = new StatusResponse();
        if (haveReferenceId) {
            statusResponse.setReferenceId(REFERENCE_ID_RETURNED);
        }
        statusResponse.setStatus(true);
        return statusResponse;
    }

    private RequestPayStatus getExpectedPayStatus() {
        Payer payer = new Payer();
        payer.setPartyId(MSISDN);
        payer.setPartyIdType(IdType.MSISDN.getValue());

        RequestPayStatus requestPayStatus = new RequestPayStatus();
        requestPayStatus.setAmount("6");
        requestPayStatus.setCurrency("EUR");
        requestPayStatus.setFinancialTransactionId("749117532");
        requestPayStatus.setExternalId("6353636");
        requestPayStatus.setPayerMessage("Pay payer message");
        requestPayStatus.setPayeeNote("Pay payee note");
        requestPayStatus.setStatus("SUCCESSFUL");
        return requestPayStatus;
    }

    private AccountBalance getExpectedAccountBalance() {
        AccountBalance accountBalance = new AccountBalance();

        accountBalance.setAvailableBalance("1000");
        accountBalance.setCurrency("EUR");
        return accountBalance;
    }

    private Result getExpectedResult() {
        return new Result(true);
    }

    private static WithdrawStatus getExpectedWithdrawStatus() {
        WithdrawStatus withdrawStatus = new WithdrawStatus();
        Payer payer = new Payer();
        payer.setPartyId(MSISDN);
        payer.setPartyIdType(IdType.MSISDN.getValue());

        withdrawStatus.setAmount("6");
        withdrawStatus.setCurrency("EUR");
        withdrawStatus.setFinancialTransactionId("749117532");
        withdrawStatus.setExternalId("6353636");
        withdrawStatus.setPayerMessage("Withdraw payer message");
        withdrawStatus.setPayeeNote("Withdraw payee note");
        withdrawStatus.setStatus("SUCCESSFUL");
        return withdrawStatus;
    }

    private static BasicUserInfo getExpectedBasicUserInfo() {
        BasicUserInfo basicUserInfo = new BasicUserInfo();
        basicUserInfo.setGiven_name("Sand");
        basicUserInfo.setFamily_name("Box");
        basicUserInfo.setBirthdate("1976-08-13");
        basicUserInfo.setLocale("sv_SE");
        basicUserInfo.setGender("MALE");
        return basicUserInfo;
    }

    private static UserInfo getExpectedUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.setSub("0");
        userInfo.setName("Sand Box");
        userInfo.setUpdated_at(1676613526L);
        userInfo.setGiven_name("Sand");
        userInfo.setFamily_name("Box");
        userInfo.setBirthdate("1976-08-13");
        userInfo.setLocale("sv_SE");
        userInfo.setGender("MALE");
        return userInfo;
    }

    private static BCAuthorize getExpectedBCAuthorize() {
        BCAuthorize bcAuthorize = new BCAuthorize();
        bcAuthorize.setAuth_req_id(UUID.randomUUID().toString());
        bcAuthorize.setExpires_in("3600");
        bcAuthorize.setInterval("5");
        return bcAuthorize;
    }

    private static Payer getPayer() {
        Payer payer = new Payer();
        payer.setPartyId(MSISDN);
        payer.setPartyIdType(IdType.MSISDN.getValue());
        return payer;
    }

    private static RequestPay getRequestPay() {
        RequestPay requestPay = new RequestPay();
        requestPay.setAmount("6.0");
        requestPay.setCurrency("EUR");
        requestPay.setExternalId("6353636");
        requestPay.setPayeeNote("Pay payee note");
        requestPay.setPayerMessage("Pay payer message");
        requestPay.setPayer(getPayer());
        return requestPay;
    }

    private static Withdraw getWithdraw() {
        Withdraw withdraw = new Withdraw();
        withdraw.setAmount("6.0");
        withdraw.setCurrency("EUR");
        withdraw.setExternalId("6353636");
        withdraw.setPayeeNote("Withdraw payee note");
        withdraw.setPayerMessage("Withdraw payer message");
        withdraw.setPayer(getPayer());
        return withdraw;
    }
}
