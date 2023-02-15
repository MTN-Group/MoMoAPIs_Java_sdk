package com.momo.api.integration;

import com.momo.api.base.HttpStatusCode;
import com.momo.api.base.constants.Constants;
import com.momo.api.base.constants.IdType;
import com.momo.api.base.context.collection.CollectionConfiguration;
import com.momo.api.base.context.disbursement.DisbursementConfiguration;
import com.momo.api.base.context.disbursement.DisbursementContext;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.model.StatusResponse;
import com.momo.api.config.MSISDN;
import com.momo.api.config.PropertiesLoader;
import com.momo.api.constants.Environment;
import com.momo.api.constants.NotificationType;
import static com.momo.api.integration.DisbursementRequestTest.loader;
import com.momo.api.models.AccountBalance;
import com.momo.api.models.AccountHolder;
import com.momo.api.models.BasicUserInfo;
import com.momo.api.models.DeliveryNotification;
import com.momo.api.models.Payee;
import com.momo.api.models.Payer;
import com.momo.api.models.Result;
import com.momo.api.models.Transfer;
import com.momo.api.models.TransferStatus;
import com.momo.api.models.collection.RequestPay;
import com.momo.api.models.disbursement.Deposit;
import com.momo.api.models.disbursement.DepositStatus;
import com.momo.api.models.disbursement.Refund;
import com.momo.api.models.disbursement.RefundStatus;
import com.momo.api.requests.collection.CollectionRequest;
import com.momo.api.requests.disbursement.DisbursementRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 *
 * Class DisbursementRequestTest
 */
public class DisbursementRequestTest {

    static PropertiesLoader loader;

    @BeforeAll
    public static void init() {
        loader = new PropertiesLoader();
    }

    private final String incorrect_referenceid = "incorrect_referenceid";
    private final static String MSISDN_NUMBER = MSISDN.SUCCESSFUL.getValue();
    private final static String SUBSCRIPTION_KEY = "DISBURSEMENT_SUBSCRIPTION_KEY";
    private final static String COLLECTION_SUBSCRIPTION_KEY = "COLLECTION_SUBSCRIPTION_KEY";

    @Test
    @DisplayName("Call Back Url Test Success")
    public void callBackUrlTest() throws MoMoException {
        //Disbursement request made without calling "addCallBackUrl(String)" with "disbursementConfiguration" object
        DisbursementConfiguration disbursementConfiguration = new DisbursementConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX);
        DisbursementRequest disbursementRequestFirst = disbursementConfiguration.createDisbursementRequest();

        StatusResponse statusResponse;

        //case 1: Passing a correct CallBackUrl with the disbursementRequestFirst object. This allows callBack's to be received for all requests made with this "disbursementRequestFirst" object.
        disbursementRequestFirst.addCallBackUrl(loader.get("CALLBACK_URL"));
        statusResponse = disbursementRequestFirst.transfer(getTransfer(MSISDN_NUMBER));

        //case 2: Passing NotificationType as "POLLING" with the disbursementRequestFirst object. No callBack's will be received for any requests made with this "disbursementRequestFirst" object unless the addCallBackUrl(String) is set again.
        disbursementRequestFirst.setNotificationType(NotificationType.POLLING);
        statusResponse = disbursementRequestFirst.transfer(getTransfer(MSISDN_NUMBER));

        //case 3: Passing a correct CallBackUrl with the disbursementConfiguration object. But will not receive callBack's since the request is made with "disbursementRequestFirst" having a "NotificationType" as "POLLING". To receive callback's again, we need to change "NotificationType" to "CALLBACK" or call the method "addCallBackUrl" with "collectionRequestFirst" with a valid callback url(as show in a later step below).
        disbursementConfiguration.addCallBackUrl(loader.get("CALLBACK_URL"));
        statusResponse = disbursementRequestFirst.transfer(getTransfer(MSISDN_NUMBER));

        //case 4: Creating a new disbursementRequestNew object and passing valid callback url. Callback will be received.
        DisbursementRequest disbursementRequestNew = disbursementConfiguration.createDisbursementRequest();
        statusResponse = disbursementRequestNew.transfer(getTransfer(MSISDN_NUMBER));

        //case 5: Correcting the CallBackUrl for "disbursementRequestFirst" object will allow callBack's to be received again using this object.
        disbursementRequestFirst.addCallBackUrl(loader.get("CALLBACK_URL"));
        statusResponse = disbursementRequestFirst.transfer(getTransfer(MSISDN_NUMBER));
    }

    @Test
    @DisplayName("Transfer Test Success")
    void transferTestSuccess() throws MoMoException {
        DisbursementConfiguration disbursementConfiguration = new DisbursementConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(loader.get("CALLBACK_URL"));
        DisbursementRequest disbursementRequest = disbursementConfiguration.createDisbursementRequest();

        Transfer transfer = getTransfer(MSISDN_NUMBER);

        StatusResponse statusResponse = disbursementRequest.transfer(transfer);
        assertNotNull(statusResponse);
        assertNotNull(statusResponse.getReferenceId());
        assertTrue(statusResponse.getStatus());
    }

    @Test
    @DisplayName("Transfer Test Failure")
    void transferTestFailure() throws MoMoException {
        DisbursementConfiguration disbursementConfiguration = new DisbursementConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(loader.get("CALLBACK_URL"));
        DisbursementRequest disbursementRequest = disbursementConfiguration.createDisbursementRequest();

        //case 1: Transfer object is null
        MoMoException moMoException = assertThrows(MoMoException.class, () -> disbursementRequest.transfer(null));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.TRANSFER_OBJECT_INIT_ERROR);

        //case 2: Transfer object initialised, but varialbes are not defined
        Transfer transfer = new Transfer();
        moMoException = assertThrows(MoMoException.class, () -> disbursementRequest.transfer(transfer));
        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.BAD_REQUEST.getHttpStatusCode()));

        //case 3: Payee object initialised, but varialbes are not defined
        transfer.setPayee(new Payee());
        moMoException = assertThrows(MoMoException.class, () -> disbursementRequest.transfer(transfer));
        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.INTERNAL_SERVER_ERROR.getHttpStatusCode()));
    }

    @Test
    @DisplayName("Transfer Status Test Success")
    void transferStatusTestSuccess() throws MoMoException {
        DisbursementConfiguration disbursementConfiguration = new DisbursementConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(loader.get("CALLBACK_URL"));
        DisbursementRequest disbursementRequest = disbursementConfiguration.createDisbursementRequest();

        Transfer transfer = getTransfer(MSISDN_NUMBER);

        StatusResponse statusResponse = disbursementRequest.transfer(transfer);

        TransferStatus transferStatus = disbursementRequest.getTransferStatus(statusResponse.getReferenceId());

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
        DisbursementConfiguration disbursementConfiguration = new DisbursementConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(loader.get("CALLBACK_URL"));
        DisbursementRequest disbursementRequest = disbursementConfiguration.createDisbursementRequest();

        //case 1: referenceId is null
        MoMoException moMoException = assertThrows(MoMoException.class, () -> disbursementRequest.getTransferStatus(null));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.NULL_VALUE_ERROR);

        //case 2: referenceId is empty
        moMoException = assertThrows(MoMoException.class, () -> disbursementRequest.getTransferStatus(""));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.EMPTY_STRING_ERROR);

        //case 3: incorrect referenceId provided
        moMoException = assertThrows(MoMoException.class, () -> disbursementRequest.getTransferStatus(incorrect_referenceid));
        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.BAD_REQUEST.getHttpStatusCode()));
    }

    @Test
    @DisplayName("Get Account Balance Test Success")
    void getAccountBalanceTestSuccess() throws MoMoException {
        DisbursementConfiguration disbursementConfiguration = new DisbursementConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX);
        DisbursementRequest disbursementRequest = disbursementConfiguration.createDisbursementRequest();

        AccountBalance accountBalance = disbursementRequest.getAccountBalance();
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
        DisbursementContext.destroySingletonObject();
        //case 1: DisbursementRequest is not properly initialised
        DisbursementRequest disbursementRequest = new DisbursementRequest();
        MoMoException moMoException = assertThrows(MoMoException.class, () -> disbursementRequest.getAccountBalance());
        assertEquals(moMoException.getError().getErrorDescription(), Constants.REQUEST_NOT_CREATED);
    }

    @Test
    @DisplayName("Get Account Balance In Specific Currency Test Success")
    void getAccountBalanceInSpecificCurrencyTestSuccess() throws MoMoException {
        DisbursementConfiguration disbursementConfiguration = new DisbursementConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX);
        DisbursementRequest disbursementRequest = disbursementConfiguration.createDisbursementRequest();

        AccountBalance accountBalance = disbursementRequest.getAccountBalanceInSpecificCurrency("EUR");
        assertNotNull(accountBalance);
        assertNotNull(accountBalance.getAvailableBalance());
        assertNotNull(accountBalance.getCurrency());
    }

    @Test
    @DisplayName("Get Account Balance In Specific Currency Test Failure")
    void getAccountBalanceInSpecificCurrencyTestFailure() throws MoMoException {
        DisbursementConfiguration disbursementConfiguration = new DisbursementConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX);
        DisbursementRequest disbursementRequest = disbursementConfiguration.createDisbursementRequest();

        //case 1: currency is null
        MoMoException moMoException = assertThrows(MoMoException.class, () -> disbursementRequest.getAccountBalanceInSpecificCurrency(null));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.NULL_VALUE_ERROR);

        //case 2: currency is empty
        moMoException = assertThrows(MoMoException.class, () -> disbursementRequest.getAccountBalanceInSpecificCurrency(""));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.EMPTY_STRING_ERROR);
    }

    @Test
    @DisplayName("Validate Account Holder Status Test Success")
    void validateAccountHolderStatusTestSuccess() throws MoMoException {
        DisbursementConfiguration disbursementConfiguration = new DisbursementConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX);
        DisbursementRequest disbursementRequest = disbursementConfiguration.createDisbursementRequest();

        AccountHolder accountHolder = new AccountHolder(IdType.MSISDN.getIdTypeLowerCase(), MSISDN_NUMBER);
        Result result = disbursementRequest.validateAccountHolderStatus(accountHolder);
        assertNotNull(result);
        assertTrue(result.getResult());
    }

    @Test
    @DisplayName("Validate Account Holder Status Test Failure")
    void validateAccountHolderStatusTestFailure() throws MoMoException {
        DisbursementConfiguration disbursementConfiguration = new DisbursementConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX);
        DisbursementRequest disbursementRequest = disbursementConfiguration.createDisbursementRequest();

        MoMoException moMoException;

        //case 1: AccountHolder is null
        moMoException = assertThrows(MoMoException.class, () -> disbursementRequest.validateAccountHolderStatus(null));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.ACCOUNT_HOLDER_OBJECT_INIT_ERROR);

        //case 2: Key or Value is null
        moMoException = assertThrows(MoMoException.class, () -> disbursementRequest.validateAccountHolderStatus(new AccountHolder(IdType.MSISDN.getIdTypeLowerCase(), null)));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.NULL_VALUE_ERROR);

        //case 3: Key or Value is empty
        moMoException = assertThrows(MoMoException.class, () -> disbursementRequest.validateAccountHolderStatus(new AccountHolder("", MSISDN_NUMBER)));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.EMPTY_STRING_ERROR);
    }

    @Test
    @DisplayName("Deposit V1 Test Success")
    void depositV1TestSuccess() throws MoMoException {
        DisbursementConfiguration disbursementConfiguration = new DisbursementConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(loader.get("CALLBACK_URL"));
        DisbursementRequest disbursementRequest = disbursementConfiguration.createDisbursementRequest();

        Deposit deposit = getDeposit(MSISDN_NUMBER);

        StatusResponse statusResponse = disbursementRequest.depositV1(deposit);
        assertNotNull(statusResponse);
        assertNotNull(statusResponse.getReferenceId());
        assertTrue(statusResponse.getStatus());
    }

    @Test
    @DisplayName("Deposit V1 Test Failure")
    void depositV1TestFailure() throws MoMoException {
        DisbursementConfiguration disbursementConfiguration = new DisbursementConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(loader.get("CALLBACK_URL"));
        DisbursementRequest disbursementRequest = disbursementConfiguration.createDisbursementRequest();

        //case 1: Deposit object is null
        MoMoException moMoException = assertThrows(MoMoException.class, () -> disbursementRequest.depositV1(null));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.DEPOSIT_OBJECT_INIT_ERROR);

        //case 2: Deposit object initialised, but varialbes are not defined
        Deposit deposit = new Deposit();
        moMoException = assertThrows(MoMoException.class, () -> disbursementRequest.depositV1(deposit));
        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.BAD_REQUEST.getHttpStatusCode()));

        //case 3: Payee object initialised, but varialbes are not defined
        deposit.setPayee(new Payee());
        moMoException = assertThrows(MoMoException.class, () -> disbursementRequest.depositV1(deposit));
        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.INTERNAL_SERVER_ERROR.getHttpStatusCode()));
    }

    @Test
    @DisplayName("Deposit V2 Test Success")
    void depositV2TestSuccess() throws MoMoException {
        DisbursementConfiguration disbursementConfiguration = new DisbursementConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(loader.get("CALLBACK_URL"));
        DisbursementRequest disbursementRequest = disbursementConfiguration.createDisbursementRequest();

        Deposit deposit = getDeposit(MSISDN_NUMBER);

        StatusResponse statusResponse = disbursementRequest.depositV2(deposit);
        assertNotNull(statusResponse);
        assertNotNull(statusResponse.getReferenceId());
        assertTrue(statusResponse.getStatus());
    }

    @Test
    @DisplayName("Deposit V2 Test Failure")
    void depositV2TestFailure() throws MoMoException {
        DisbursementConfiguration disbursementConfiguration = new DisbursementConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(loader.get("CALLBACK_URL"));
        DisbursementRequest disbursementRequest = disbursementConfiguration.createDisbursementRequest();

        //case 1: Deposit object is null
        MoMoException moMoException = assertThrows(MoMoException.class, () -> disbursementRequest.depositV2(null));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.DEPOSIT_OBJECT_INIT_ERROR);

        //case 2: Deposit object initialised, but varialbes are not defined
        Deposit deposit = new Deposit();
        moMoException = assertThrows(MoMoException.class, () -> disbursementRequest.depositV2(deposit));
        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.BAD_REQUEST.getHttpStatusCode()));

        //case 3: Payee object initialised, but varialbes are not defined
        deposit.setPayee(new Payee());
        moMoException = assertThrows(MoMoException.class, () -> disbursementRequest.depositV2(deposit));
        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.INTERNAL_SERVER_ERROR.getHttpStatusCode()));
    }

    @Test
    @DisplayName("Get Deposit Status Test Success")
    void getDepositStatusTestSuccess() throws MoMoException {
        DisbursementConfiguration disbursementConfiguration = new DisbursementConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(loader.get("CALLBACK_URL"));
        DisbursementRequest disbursementRequest = disbursementConfiguration.createDisbursementRequest();

        Deposit deposit = getDeposit(MSISDN_NUMBER);

        StatusResponse statusResponse = disbursementRequest.depositV1(deposit);

        DepositStatus depositStatus = disbursementRequest.getDepositStatus(statusResponse.getReferenceId());

        assertNotNull(depositStatus);
        assertNotNull(depositStatus.getAmount());
        assertNotNull(depositStatus.getCurrency());
        assertNotNull(depositStatus.getExternalId());
//        assertNotNull(depositStatus.getFinancialTransactionId());
        assertNotNull(depositStatus.getPayeeNote());
        assertNotNull(depositStatus.getPayerMessage());
        //assertNotNull(payStatus.getReason());
        assertNotNull(depositStatus.getStatus());

        assertNotNull(depositStatus.getPayee());
        Payee payee = depositStatus.getPayee();
        assertNotNull(payee.getPartyId());
        assertNotNull(payee.getPartyIdType());
    }

    @Test
    @DisplayName("Get Deposit Status Test Failure")
    void getDepositStatusTestFailure() throws MoMoException {
        DisbursementConfiguration disbursementConfiguration = new DisbursementConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(loader.get("CALLBACK_URL"));
        DisbursementRequest disbursementRequest = disbursementConfiguration.createDisbursementRequest();

        //case 1: referenceId is null
        MoMoException moMoException = assertThrows(MoMoException.class, () -> disbursementRequest.getDepositStatus(null));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.NULL_VALUE_ERROR);

        //case 2: referenceId is empty
        moMoException = assertThrows(MoMoException.class, () -> disbursementRequest.getDepositStatus(""));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.EMPTY_STRING_ERROR);

        //case 3: incorrect referenceId provided
        moMoException = assertThrows(MoMoException.class, () -> disbursementRequest.getDepositStatus(incorrect_referenceid));
        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.BAD_REQUEST.getHttpStatusCode()));
    }

    @Test
    @DisplayName("Refund V1 Test Success")
    void refundV1TestSuccess() throws MoMoException {
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(COLLECTION_SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(loader.get("CALLBACK_URL"));
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        RequestPay requestPay = getRequestPay(MSISDN_NUMBER);

        StatusResponse statusResponse = collectionRequest.requestToPay(requestPay);

        DisbursementConfiguration disbursementConfiguration = new DisbursementConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(loader.get("CALLBACK_URL"));
        DisbursementRequest disbursementRequest = disbursementConfiguration.createDisbursementRequest();

        Refund refund = getRefund(statusResponse.getReferenceId());

        statusResponse = disbursementRequest.refundV1(refund);
        assertNotNull(statusResponse);
        assertNotNull(statusResponse.getReferenceId());
        assertTrue(statusResponse.getStatus());
    }

    @Test
    @DisplayName("Refund V1 Test Failure")
    void refundV1TestFailure() throws MoMoException {
        DisbursementConfiguration disbursementConfiguration = new DisbursementConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(loader.get("CALLBACK_URL"));
        DisbursementRequest disbursementRequest = disbursementConfiguration.createDisbursementRequest();

        //case 1: Refund object is null
        MoMoException moMoException = assertThrows(MoMoException.class, () -> disbursementRequest.refundV1(null));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.REFUND_OBJECT_INIT_ERROR);

        //case 2: Refund object initialised, but varialbes are not defined
        Refund refund = new Refund();
        moMoException = assertThrows(MoMoException.class, () -> disbursementRequest.refundV1(refund));
        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.BAD_REQUEST.getHttpStatusCode()));

        //case 3: Invalid reference id
        Refund refundInvalid = getRefund(incorrect_referenceid);
        moMoException = assertThrows(MoMoException.class, () -> disbursementRequest.refundV1(refundInvalid));
        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.BAD_REQUEST.getHttpStatusCode()));
    }

    @Test
    @DisplayName("Refund V2 Test Success")
    void refundV2TestSuccess() throws MoMoException {
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(COLLECTION_SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(loader.get("CALLBACK_URL"));
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        RequestPay requestPay = getRequestPay(MSISDN_NUMBER);

        StatusResponse statusResponse = collectionRequest.requestToPay(requestPay);

        DisbursementConfiguration disbursementConfiguration = new DisbursementConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(loader.get("CALLBACK_URL"));
        DisbursementRequest disbursementRequest = disbursementConfiguration.createDisbursementRequest();

        Refund refund = getRefund(statusResponse.getReferenceId());

        statusResponse = disbursementRequest.refundV2(refund);
        assertNotNull(statusResponse);
        assertNotNull(statusResponse.getReferenceId());
        assertTrue(statusResponse.getStatus());
    }

    @Test
    @DisplayName("Refund V2 Test Failure")
    void refundV2TestFailure() throws MoMoException {
        DisbursementConfiguration disbursementConfiguration = new DisbursementConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(loader.get("CALLBACK_URL"));
        DisbursementRequest disbursementRequest = disbursementConfiguration.createDisbursementRequest();

        //case 1: Refund object is null
        MoMoException moMoException = assertThrows(MoMoException.class, () -> disbursementRequest.refundV2(null));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.REFUND_OBJECT_INIT_ERROR);

        //case 2: Refund object initialised, but varialbes are not defined
        Refund refund = new Refund();
        moMoException = assertThrows(MoMoException.class, () -> disbursementRequest.refundV2(refund));
        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.BAD_REQUEST.getHttpStatusCode()));

        //case 3: Invalid reference id
        Refund refundInvalid = getRefund(incorrect_referenceid);
        moMoException = assertThrows(MoMoException.class, () -> disbursementRequest.refundV2(refundInvalid));
        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.BAD_REQUEST.getHttpStatusCode()));
    }

    @Test
    @DisplayName("Get Refund Status Test Success")
    void getRefundStatusTestSuccess() throws MoMoException {
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(COLLECTION_SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(loader.get("CALLBACK_URL"));
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        RequestPay requestPay = getRequestPay(MSISDN_NUMBER);

        StatusResponse statusResponse = collectionRequest.requestToPay(requestPay);

        DisbursementConfiguration disbursementConfiguration = new DisbursementConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(loader.get("CALLBACK_URL"));
        DisbursementRequest disbursementRequest = disbursementConfiguration.createDisbursementRequest();

        Refund refund = getRefund(statusResponse.getReferenceId());

        statusResponse = disbursementRequest.refundV1(refund);

        RefundStatus refundStatus = disbursementRequest.getRefundStatus(statusResponse.getReferenceId());

        assertNotNull(refundStatus);
        assertNotNull(refundStatus.getAmount());
        assertNotNull(refundStatus.getCurrency());
        assertNotNull(refundStatus.getExternalId());
//        assertNotNull(refundStatus.getFinancialTransactionId());
        assertNotNull(refundStatus.getPayeeNote());
        assertNotNull(refundStatus.getPayerMessage());
        //assertNotNull(payStatus.getReason());
        assertNotNull(refundStatus.getStatus());

        assertNotNull(refundStatus.getPayee());
        Payee payee = refundStatus.getPayee();
        assertNotNull(payee.getPartyId());
        assertNotNull(payee.getPartyIdType());
    }

    @Test
    @DisplayName("Get Refund Status Test Failure")
    void getRefundStatusTestFailure() throws MoMoException {
        DisbursementConfiguration disbursementConfiguration = new DisbursementConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(loader.get("CALLBACK_URL"));
        DisbursementRequest disbursementRequest = disbursementConfiguration.createDisbursementRequest();

        //case 1: referenceId is null
        MoMoException moMoException = assertThrows(MoMoException.class, () -> disbursementRequest.getRefundStatus(null));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.NULL_VALUE_ERROR);

        //case 2: referenceId is empty
        moMoException = assertThrows(MoMoException.class, () -> disbursementRequest.getRefundStatus(""));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.EMPTY_STRING_ERROR);

        //case 3: incorrect referenceId provided
        moMoException = assertThrows(MoMoException.class, () -> disbursementRequest.getRefundStatus(incorrect_referenceid));
        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.BAD_REQUEST.getHttpStatusCode()));
    }

    @Test
    @DisplayName("Request To Pay Delivery Notification Test Success")
    void requestToPayDeliveryNotificationTestSuccess() throws MoMoException {
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(COLLECTION_SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(loader.get("CALLBACK_URL"));
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        RequestPay requestPay = getRequestPay(MSISDN_NUMBER);
        StatusResponse statusResponsePay = collectionRequest.requestToPay(requestPay);

        DeliveryNotification deliveryNotification = new DeliveryNotification();
        deliveryNotification.setNotificationMessage("test message");

        DisbursementConfiguration disbursementConfiguration = new DisbursementConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(loader.get("CALLBACK_URL"));
        DisbursementRequest disbursementRequest = disbursementConfiguration.createDisbursementRequest();

        StatusResponse statusResponseDeliveryNotification = disbursementRequest.requestToPayDeliveryNotification(statusResponsePay.getReferenceId(), deliveryNotification);
        assertEquals(statusResponseDeliveryNotification.getStatus(), true);
    }

    @Test
    @DisplayName("Request To Pay Delivery Notification Test Failure")
    void requestToPayDeliveryNotificationTestFailure() throws MoMoException {
        DisbursementConfiguration disbursementConfiguration = new DisbursementConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(loader.get("CALLBACK_URL"));
        DisbursementRequest disbursementRequest = disbursementConfiguration.createDisbursementRequest();

        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(loader.get(COLLECTION_SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(loader.get("CALLBACK_URL"));
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();
        RequestPay requestPay = getRequestPay(MSISDN_NUMBER);
        StatusResponse statusResponsePay = collectionRequest.requestToPay(requestPay);

        DeliveryNotification deliveryNotification = new DeliveryNotification();
        deliveryNotification.setNotificationMessage("test message");

        //case 1: referenceId given is not from request to pay
        MoMoException moMoException = assertThrows(MoMoException.class, () -> disbursementRequest.requestToPayDeliveryNotification(incorrect_referenceid, deliveryNotification));
        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.BAD_REQUEST.getHttpStatusCode()));

        //case 7: Using the same referenceId for requestToPayDeliveryNotification after a successful request was made
        deliveryNotification.setNotificationMessage("test message");
        //making the first request
        StatusResponse statusResponseDeliveryNotification = disbursementRequest.requestToPayDeliveryNotification(statusResponsePay.getReferenceId(), deliveryNotification, "Header Message");
        assertEquals(statusResponseDeliveryNotification.getStatus(), true);
        //making the second request
        moMoException = assertThrows(MoMoException.class, () -> disbursementRequest.requestToPayDeliveryNotification(statusResponsePay.getReferenceId(), deliveryNotification, "Header Message"));
        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.TOO_MANY_REQUESTS.getHttpStatusCode()));

    }

    @Test
    @DisplayName("Get Basic Userinfo Test Success")
    void getBasicUserinfoTestSuccess() throws MoMoException {
        DisbursementConfiguration disbursementConfiguration = new DisbursementConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX);
        DisbursementRequest disbursementRequest = disbursementConfiguration.createDisbursementRequest();

        BasicUserInfo basicUserInfo = disbursementRequest.getBasicUserinfo(MSISDN_NUMBER);
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
        DisbursementConfiguration disbursementConfiguration = new DisbursementConfiguration(loader.get(SUBSCRIPTION_KEY), loader.get("REFERENCE_ID"), loader.get("API_KEY"), Environment.SANDBOX, Constants.SANDBOX);
        DisbursementRequest disbursementRequest = disbursementConfiguration.createDisbursementRequest();

        MoMoException moMoException;

        //case 1: Value is null
        moMoException = assertThrows(MoMoException.class, () -> disbursementRequest.getBasicUserinfo(null));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.NULL_VALUE_ERROR);

        //case 2: Value is empty
        moMoException = assertThrows(MoMoException.class, () -> disbursementRequest.getBasicUserinfo(""));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.EMPTY_STRING_ERROR);
    }

    private static Payee getPayee(String msisdnValue) {
        Payee payee = new Payee();
        payee.setPartyId(msisdnValue);
        payee.setPartyIdType(IdType.MSISDN.getIdType());
        return payee;
    }

    private static Transfer getTransfer(MSISDN msisdnObject) {
        return getTransfer(msisdnObject.getValue());
    }

    private static Transfer getTransfer(String msisdnValue) {
        Transfer transfer = new Transfer();
        transfer.setAmount("6.0");
        transfer.setCurrency("EUR");
        transfer.setExternalId(getExternalId());
        transfer.setPayeeNote("Transfer payee note");
        transfer.setPayerMessage("Transfer payer message");
        transfer.setPayee(getPayee(msisdnValue));
        return transfer;
    }

    private static Deposit getDeposit(MSISDN msisdnObject) {
        return getDeposit(msisdnObject.getValue());
    }

    private static Deposit getDeposit(String msisdnValue) {
        Deposit deposit = new Deposit();
        deposit.setAmount("6.0");
        deposit.setCurrency("EUR");
        deposit.setExternalId(getExternalId());
        deposit.setPayeeNote("Deposit payee note");
        deposit.setPayerMessage("Deposit payer message");
        deposit.setPayee(getPayee(msisdnValue));
        return deposit;
    }

    private static Refund getRefund(String referenceId) {
        Refund refund = new Refund();
        refund.setAmount("6.0");
        refund.setCurrency("EUR");
        refund.setExternalId(getExternalId());
        refund.setPayeeNote("Refund payee note");
        refund.setPayerMessage("Refund payer message");
        refund.setReferenceIdToRefund(referenceId);
        return refund;
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

    private static String getExternalId() {
        String currentTimeMillis = Long.toString(System.currentTimeMillis());
        return currentTimeMillis.substring(currentTimeMillis.length() - 6);
    }
}
