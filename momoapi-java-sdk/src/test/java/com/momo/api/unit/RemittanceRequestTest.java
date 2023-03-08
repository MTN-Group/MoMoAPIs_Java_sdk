package com.momo.api.unit;

import com.momo.api.base.constants.AccessType;
import com.momo.api.base.constants.Constants;
import com.momo.api.base.constants.IdType;
import com.momo.api.base.constants.TargetEnvironment;
import com.momo.api.base.context.remittance.RemittanceConfiguration;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.model.BCAuthorize;
import com.momo.api.base.model.StatusResponse;
import com.momo.api.constants.Environment;
import com.momo.api.models.*;
import com.momo.api.requests.remittance.RemittanceRequest;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

/**
 *
 * Class RemittanceRequestTest
 */
public class RemittanceRequestTest {
    
    private static final String REFERENCE_ID_RETURNED = UUID.randomUUID().toString();
    private static final String REFERENCE_ID_PARAMETER = UUID.randomUUID().toString();
    private static final String MSISDN = "23423423450";

    @Test
    @DisplayName("Remittance Configuration Test Success")
    void remittanceConfigurationTestFailure() throws MoMoException {
        MoMoException moMoException = assertThrows(MoMoException.class, () -> new RemittanceConfiguration(
                "",
                "REFERENCE_ID",
                "API_KEY",
                Environment.SANDBOX,
                TargetEnvironment.sandbox.getValue()));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.EMPTY_STRING_ERROR);
        moMoException = assertThrows(MoMoException.class, () -> new RemittanceConfiguration(
                "SUBSCRIPTION_KEY",
                null,
                "API_KEY",
                Environment.SANDBOX,
                TargetEnvironment.sandbox.getValue()));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.NULL_VALUE_ERROR);
        moMoException = assertThrows(MoMoException.class, () -> new RemittanceConfiguration(
                "SUBSCRIPTION_KEY",
                "REFERENCE_ID",
                "API_KEY",
                null,
                TargetEnvironment.sandbox.getValue()));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.NULL_VALUE_ERROR);
    }

    @Test
    @DisplayName("Transfer Test Success")
    void transferTestSuccess() throws MoMoException {
        RemittanceRequest remittanceRequestSpy = spy(new RemittanceRequest());
        
        Transfer transfer = getTransfer();
        StatusResponse expectedStatusResponse = getExpectedStatusResponse(true);
        doReturn(expectedStatusResponse).when(remittanceRequestSpy).transfer(transfer);
        
        StatusResponse statusResponse = remittanceRequestSpy.transfer(transfer);
        assertNotNull(statusResponse);
        assertNotNull(statusResponse.getReferenceId());
        assertEquals(statusResponse.getReferenceId(), expectedStatusResponse.getReferenceId());
    }

    @Test
    @DisplayName("Transfer Test Failure")
    void transferTestFailure() throws MoMoException {
        RemittanceRequest remittanceRequestSpy = spy(new RemittanceRequest());
        
        Transfer transfer = getTransfer();
        doThrow(MoMoException.class).when(remittanceRequestSpy).transfer(transfer);
        
        assertThrows(MoMoException.class, () -> remittanceRequestSpy.transfer(transfer));
    }

    @Test
    @DisplayName("Transfer Status Test Success")
    void transferStatusTestSuccess() throws MoMoException {
        RemittanceRequest remittanceRequestSpy = spy(new RemittanceRequest());
        
        TransferStatus expectedTransferStatus = getExpectedTransferStatus();
        doReturn(expectedTransferStatus).when(remittanceRequestSpy).getTransferStatus(REFERENCE_ID_PARAMETER);
        
        TransferStatus transferStatus = remittanceRequestSpy.getTransferStatus(REFERENCE_ID_PARAMETER);
        assertNotNull(transferStatus);
        assertNotNull(transferStatus.getAmount());
        assertEquals(transferStatus.getAmount(), expectedTransferStatus.getAmount());
    }

    @Test
    @DisplayName("Transfer Status Test Failure")
    void transferStatusTestFailure() throws MoMoException {
        RemittanceRequest remittanceRequestSpy = spy(new RemittanceRequest());
        
        doThrow(MoMoException.class).when(remittanceRequestSpy).getTransferStatus(REFERENCE_ID_PARAMETER);
        
        assertThrows(MoMoException.class, () -> remittanceRequestSpy.getTransferStatus(REFERENCE_ID_PARAMETER));
    }
    
    @Test
    @DisplayName("Validate Account Holder Status Test Success")
    void validateAccountHolderStatusTestSuccess() throws MoMoException{
        RemittanceRequest remittanceRequestSpy = spy(new RemittanceRequest());
        
        Result expectedResult = getExpectedResult();
        AccountHolder accountHolder = new AccountHolder(IdType.msisdn.getValue(), MSISDN);
        doReturn(expectedResult).when(remittanceRequestSpy).validateAccountHolderStatus(accountHolder);

        Result actualResult = remittanceRequestSpy.validateAccountHolderStatus(accountHolder);
        assertNotNull(actualResult);
        assertEquals(actualResult.getResult(), expectedResult.getResult());
    }

    @Test
    @DisplayName("Validate Account Holder Status Test Failure")
    void validateAccountHolderStatusTestFailure() throws MoMoException {
        RemittanceRequest remittanceRequestSpy = spy(new RemittanceRequest());
        
        AccountHolder accountHolder = new AccountHolder(IdType.msisdn.getValue(), MSISDN);
        doThrow(MoMoException.class).when(remittanceRequestSpy).validateAccountHolderStatus(accountHolder);
        
        assertThrows(MoMoException.class, () -> remittanceRequestSpy.validateAccountHolderStatus(accountHolder));
    }
    
    @Test
    @DisplayName("Get Account Balance Test Success")
    void getAccountBalanceTestSuccess() throws MoMoException{
        RemittanceRequest remittanceRequestSpy = spy(new RemittanceRequest());
        
        AccountBalance expectedAccountBalance = getExpectedAccountBalance();
        doReturn(expectedAccountBalance).when(remittanceRequestSpy).getAccountBalance();

        AccountBalance actualAccountBalance = remittanceRequestSpy.getAccountBalance();
        assertNotNull(actualAccountBalance);
        assertNotNull(actualAccountBalance.getAvailableBalance());
        assertEquals(actualAccountBalance.getAvailableBalance(), expectedAccountBalance.getAvailableBalance());
    }
    
    @Test
    @DisplayName("Get Account Balance Test Failure")
    void getAccountBalanceTestFailure() throws MoMoException{
        RemittanceRequest remittanceRequestSpy = spy(new RemittanceRequest());
        
        doThrow(MoMoException.class).when(remittanceRequestSpy).getAccountBalance();
        
        assertThrows(MoMoException.class, () -> remittanceRequestSpy.getAccountBalance());
    }
    
    @Test
    @DisplayName("Get Basic Userinfo Test Success")
    void getBasicUserinfoTestSuccess() throws MoMoException{
        RemittanceRequest remittanceRequestSpy = spy(new RemittanceRequest());

        BasicUserInfo expectedBasicUserInfo = getExpectedBasicUserInfo();
        doReturn(expectedBasicUserInfo).when(remittanceRequestSpy).getBasicUserinfo(MSISDN);
        
        BasicUserInfo basicUserInfo = remittanceRequestSpy.getBasicUserinfo(MSISDN);
        assertNotNull(basicUserInfo);
        assertNotNull(basicUserInfo.getBirthdate());
        assertEquals(basicUserInfo.getBirthdate(), expectedBasicUserInfo.getBirthdate());
    }

    @Test
    @DisplayName("Get Basic Userinfo Test Failure")
    void getBasicUserinfoTestFailure() throws MoMoException {
        RemittanceRequest remittanceRequestSpy = spy(new RemittanceRequest());
        
        doThrow(MoMoException.class).when(remittanceRequestSpy).getBasicUserinfo(MSISDN);
        
        assertThrows(MoMoException.class, () -> remittanceRequestSpy.getBasicUserinfo(MSISDN));
    }

    @Test
    @DisplayName("Request To Pay Delivery Notification Test Success")
    void requestToPayDeliveryNotificationTestSuccess() throws MoMoException {
        RemittanceRequest remittanceRequestSpy = spy(new RemittanceRequest());

        DeliveryNotification deliveryNotification = new DeliveryNotification();
        deliveryNotification.setNotificationMessage("test message");

        StatusResponse expectedStatusResponse = getExpectedStatusResponse(false);
        doReturn(expectedStatusResponse).when(remittanceRequestSpy).requestToPayDeliveryNotification(REFERENCE_ID_PARAMETER, deliveryNotification, "Header Message", "eng");

        StatusResponse statusResponse = remittanceRequestSpy.requestToPayDeliveryNotification(REFERENCE_ID_PARAMETER, deliveryNotification, "Header Message", "eng");
        assertNotNull(statusResponse);
        assertNotNull(statusResponse.getStatus());
        assertEquals(statusResponse.getReferenceId(), null);
        assertEquals(statusResponse.getStatus(), expectedStatusResponse.getStatus());
    }

    @Test
    @DisplayName("Request To Pay Delivery Notification Test Failure")
    void requestToPayDeliveryNotificationTestFailure() throws MoMoException {
        RemittanceRequest remittanceRequestSpy = spy(new RemittanceRequest());
        
        DeliveryNotification deliveryNotification = new DeliveryNotification();
        doThrow(MoMoException.class).when(remittanceRequestSpy).requestToPayDeliveryNotification(REFERENCE_ID_PARAMETER, deliveryNotification, "Header Message", "eng");
        
        assertThrows(MoMoException.class, () -> remittanceRequestSpy.requestToPayDeliveryNotification(REFERENCE_ID_PARAMETER, deliveryNotification, "Header Message", "eng"));
    }

    @Test
    @DisplayName("Get Userinfo With Consent Test Success")
    void getUserInfoWithConsentTestSuccess() throws MoMoException {
        RemittanceRequest remittanceRequestSpy = spy(new RemittanceRequest());

        AccountHolder accountHolder = new AccountHolder(IdType.MSISDN.getValue(), MSISDN);
        UserInfo expectedUserInfo = getExpectedUserInfo();
        doReturn(expectedUserInfo).when(remittanceRequestSpy).getUserInfoWithConsent(accountHolder, "profile", AccessType.OFFLINE);

        UserInfo userInfo = remittanceRequestSpy.getUserInfoWithConsent(accountHolder, "profile", AccessType.OFFLINE);

        assertEquals(userInfo.getName(), expectedUserInfo.getName());
    }

    @Test
    @DisplayName("Get Userinfo With Consent Test Failure")
    void getUserInfoWithConsentTestFailure() throws MoMoException {
        RemittanceRequest remittanceRequestSpy = spy(new RemittanceRequest());

        AccountHolder accountHolder = new AccountHolder(IdType.MSISDN.getValue(), MSISDN);
        doThrow(MoMoException.class).when(remittanceRequestSpy).getUserInfoWithConsent(accountHolder, "profile", AccessType.OFFLINE);

        assertThrows(MoMoException.class, () -> remittanceRequestSpy.getUserInfoWithConsent(accountHolder, "profile", AccessType.OFFLINE));
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
    
    private StatusResponse getExpectedStatusResponse(boolean haveReferenceId) {
        StatusResponse statusResponse = new StatusResponse();
        if(haveReferenceId){
            statusResponse.setReferenceId(REFERENCE_ID_RETURNED);
        }
        statusResponse.setStatus(true);
        return statusResponse;
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

    private static BasicUserInfo getExpectedBasicUserInfo() {
        BasicUserInfo basicUserInfo = new BasicUserInfo();
        basicUserInfo.setGiven_name("Sand");
        basicUserInfo.setFamily_name("Box");
        basicUserInfo.setBirthdate("1976-08-13");
        basicUserInfo.setLocale("sv_SE");
        basicUserInfo.setGender("MALE");
        return basicUserInfo;
    }
    
    private TransferStatus getExpectedTransferStatus(){
        
        Payee payee = new Payee();
        payee.setPartyId(MSISDN);
        payee.setPartyIdType(IdType.MSISDN.getValue());

        TransferStatus transferStatus = new TransferStatus();
        transferStatus.setAmount("6");
        transferStatus.setCurrency("EUR");
        transferStatus.setFinancialTransactionId("1220513305");
        transferStatus.setExternalId("6353636");
        transferStatus.setPayerMessage("Transfer payer message");
        transferStatus.setPayeeNote("Transfer payee note");
        transferStatus.setStatus("SUCCESSFUL");
        return transferStatus;
    }
    
    private static Payee getPayee() {
        Payee payee = new Payee();
        payee.setPartyId(MSISDN);
        payee.setPartyIdType(IdType.MSISDN.getValue());
        return payee;
    }

    private static Transfer getTransfer() {
        Transfer transfer = new Transfer();
        transfer.setAmount("6.0");
        transfer.setCurrency("EUR");
        transfer.setExternalId("6353636");
        transfer.setPayeeNote("Transfer payee note");
        transfer.setPayerMessage("Transfer payer message");
        transfer.setPayee(getPayee());
        return transfer;
    }
}
