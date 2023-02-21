package com.momo.api;

import com.momo.api.base.constants.AccessType;
import com.momo.api.base.constants.Constants;
import com.momo.api.base.constants.IdType;
import com.momo.api.base.context.collection.CollectionConfiguration;
import com.momo.api.base.context.collection.CollectionContext;
import com.momo.api.base.context.disbursement.DisbursementConfiguration;
import com.momo.api.base.context.disbursement.DisbursementContext;
import com.momo.api.base.context.provisioning.UserProvisioningConfiguration;
import com.momo.api.requests.provisioning.UserProvisioningRequest;
import com.momo.api.base.context.remittance.RemittanceConfiguration;
import com.momo.api.base.context.remittance.RemittanceContext;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.model.BCAuthorize;
import com.momo.api.base.model.StatusResponse;
import com.momo.api.base.util.JSONFormatter;
import com.momo.api.constants.Environment;
import com.momo.api.models.collection.RequestPay;
import com.momo.api.requests.collection.CollectionRequest;
import com.momo.api.models.AccountBalance;
import com.momo.api.models.AccountHolder;
import com.momo.api.models.ApiKey;
import com.momo.api.models.ApiUser;
import com.momo.api.models.BasicUserInfo;
import com.momo.api.models.CallbackHost;
import com.momo.api.models.DeliveryNotification;
import com.momo.api.models.Payee;
import com.momo.api.models.Payer;
import com.momo.api.models.Result;
import com.momo.api.models.disbursement.Deposit;
import com.momo.api.models.disbursement.DepositStatus;
import com.momo.api.models.disbursement.Refund;
import com.momo.api.models.disbursement.RefundStatus;
import com.momo.api.models.Transfer;
import com.momo.api.models.TransferStatus;
import com.momo.api.models.UserInfo;
import com.momo.api.models.collection.RequestPayStatus;
import com.momo.api.models.collection.Withdraw;
import com.momo.api.models.collection.WithdrawStatus;
import com.momo.api.requests.disbursement.DisbursementRequest;
import com.momo.api.requests.remittance.RemittanceRequest;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MoMoApiSDK {

    private static final String CALLBACK_URL = "http://webhook.site/b6491e9b-8068-4703-8e21-ede56749c28a";
    private static final String COLLECTION_SUBSCRIPTION_KEY = "063554d88fa74ed59b7b81b9c588c3a6";//sanal
//    private static final String COLLECTION_SUBSCRIPTION_KEY = "c42e4c910c7d4a18bd8e0f69b40ca1c7";//mrudul
    private static final String DISBURSEMENT_SUBSCRIPTION_KEY = "4c79e757933140e78f539726b030e85d";//sanal
    private static final String REMITTANCE_SUBSCRIPTION_KEY = "304ead483efb437ab618ddbe798c6c21";//sanal
//    private static final String REMITTANCE_SUBSCRIPTION_KEY = "46f3cec94ca449d89ee429ce4d2ecc73";//mrudul
    //requestToPay::referenceId : 142582e4-88bd-4f2d-a552-246244e081eb

    private static final String SUBSCRIPTION_KEY = COLLECTION_SUBSCRIPTION_KEY;
    private static String REFERENCE_ID = "edd1154d-88f1-4ec9-9c33-66a4f40cc023";
    private static String API_KEY = "63cb6670c44449a9a5fda69b5197c143";

    private static String requestToPayReferenceId = "";

    public static void main(String[] args) throws MoMoException, NoSuchFieldException {

        List<String> MSISDNs = Arrays.asList(
                //                                "46733123450", 
                //                                "46733123451", 
                //                                "46733123452", 
                //                                "46733123453", 
                //                                "46733123454", 
                //                                "46733123455",
                //                                "46733123456",
                //                                "29835739428",
                //                                "23423423442",
                "23423423450"
        );
        try {
//            userProvisioning();
//            userProvisioningConfiguration(false, DISBURSEMENT_SUBSCRIPTION_KEY);
            for (String MSISDN : MSISDNs) {
//                moMoExceptionSample(MSISDN);
//                getAccessToken(MSISDN, COLLECTION_SUBSCRIPTION_KEY);
//                collection(MSISDN);
//                requestToPayReferenceId = "a0fc40b6-fc42-4cc3-ac71-1adc5b009217";
                disbursement(MSISDN, requestToPayReferenceId);
//                remittance(MSISDN, requestToPayReferenceId);
//                collectionDisbursementRemittance(MSISDN);
            }
        } catch (MoMoException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            Logger.getLogger(MoMoApiSDK.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void userProvisioningConfiguration(boolean updatePropertiesFIle, String SUBSCRIPTION_KEY_VALUE) throws MoMoException, NoSuchFieldException, UnsupportedEncodingException {
        CallbackHost callbackHost = new CallbackHost("webhook.site");
        UserProvisioningConfiguration userProvisioningConfiguration = new UserProvisioningConfiguration(SUBSCRIPTION_KEY_VALUE);
        UserProvisioningRequest userProvisioningRequest = userProvisioningConfiguration.createUserProvisioningRequest();

        StatusResponse statusResponse = userProvisioningRequest.createUser(callbackHost);//ef1198cc-abeb-4fa9-a796-9712b2b9250f
        System.out.println("referenceId: " + statusResponse.getReferenceId());
        REFERENCE_ID = statusResponse.getReferenceId();
//        String referenceId = "ef1198cc-abeb-4fa9-a796-9712b2b9250f";
        ApiUser apiUser = userProvisioningRequest.getUserDetails(statusResponse.getReferenceId());
        System.out.println("ApiUser: " + JSONFormatter.toJSON(apiUser));
        ApiKey apiKey = userProvisioningRequest.createApiKey(statusResponse.getReferenceId());
        API_KEY = apiKey.getApiKey();
        System.out.println("ApiKey: " + API_KEY);
        System.out.println("Base64: " + generateBase64String(REFERENCE_ID, API_KEY));

        if (updatePropertiesFIle) {
            Properties properties = new Properties();
            try ( InputStream input = new FileInputStream("src/test/resources/config.properties")) {
                properties.load(input);
            } catch (IOException io) {
                io.printStackTrace();
            }
            try ( OutputStream outputStream = new FileOutputStream("src/test/resources/config.properties")) {
                properties.setProperty("REFERENCE_ID", REFERENCE_ID);
                properties.setProperty("API_KEY", API_KEY);
                properties.store(outputStream, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static void userProvisioning() throws MoMoException, NoSuchFieldException {
////        UserProvisioning userProvisioning = new UserProvisioning(DISBURSEMENT_SUBSCRIPTION_KEY);
//        UserProvisioning userProvisioning = new UserProvisioning(DISBURSEMENT_SUBSCRIPTION_KEY);
//
//        ApiUser apiUser = new ApiUser();
//        apiUser.setProviderCallbackHost("webhook.site");
//
//        String referenceId = userProvisioning.createUser(apiUser);//ef1198cc-abeb-4fa9-a796-9712b2b9250f
//        System.out.println("referenceId: " + referenceId);
//        REFERENCE_ID = referenceId;
////        String referenceId = "ef1198cc-abeb-4fa9-a796-9712b2b9250f";
//        apiUser = userProvisioning.getUserDetails(referenceId);
//        System.out.println("ApiUser: " + apiUser.toJSON());
//        API_KEY = userProvisioning.generateApiKey();
//        System.out.println("ApiKey: " + API_KEY);
    }
    
    private static void moMoExceptionSample(String MSISDN) throws MoMoException {
        System.out.println(":::::::::MSISDN: " + MSISDN);

        Payer payer = new Payer();
        payer.setPartyId(MSISDN);
        payer.setPartyIdType(IdType.MSISDN.getValue());

        RequestPay requestPay = new RequestPay();
        requestPay.setAmount("6.0");
        requestPay.setCurrency("EUR");
        requestPay.setExternalId("6353636");
        requestPay.setPayeeNote("payer note");
        requestPay.setPayerMessage("Pay for product a");
        requestPay.setPayer(payer);

        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(COLLECTION_SUBSCRIPTION_KEY, REFERENCE_ID, API_KEY, Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(CALLBACK_URL);
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        try {
            collectionRequest.requestToPay(null);
        } catch (MoMoException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void collectionDisbursementRemittance(String MSISDN) throws MoMoException {
        System.out.println(":::::::::MSISDN: " + MSISDN);
        StatusResponse statusResponse = null;

        RequestPay requestPay = getPay(MSISDN);

        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(COLLECTION_SUBSCRIPTION_KEY, REFERENCE_ID, API_KEY, Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(CALLBACK_URL);
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        System.out.println("requestToPay");
        statusResponse = collectionRequest.requestToPay(requestPay);
        System.out.println("statusResponse : " + JSONFormatter.toJSON(statusResponse));

//        String referenceId = "18566e1d-971f-4564-a7ec-6f10d7c87e4e";
        String referenceId = statusResponse.getReferenceId();

        Transfer transfer = getTransfer(MSISDN);

        DisbursementConfiguration disbursementConfiguration = new DisbursementConfiguration(DISBURSEMENT_SUBSCRIPTION_KEY, REFERENCE_ID, API_KEY).addCallBackUrl(CALLBACK_URL);
        DisbursementRequest disbursementRequest = disbursementConfiguration.createDisbursementRequest();

        try {
            //referenceId from request to pay of collection
            Refund refund = getRefund(referenceId);

            System.out.println("refundV1");
            statusResponse = disbursementRequest.refundV1(refund);
            System.out.println("statusResponse : " + JSONFormatter.toJSON(statusResponse));
        } catch (MoMoException e) {
            Logger.getLogger(MoMoApiSDK.class.getName()).log(Level.SEVERE, null, e);
        }

        RemittanceConfiguration remittanceConfiguration = new RemittanceConfiguration(REMITTANCE_SUBSCRIPTION_KEY, REFERENCE_ID, API_KEY).addCallBackUrl(CALLBACK_URL);
        RemittanceRequest remittanceRequest = remittanceConfiguration.createRemittanceRequest();

        DeliveryNotification deliveryNotification = new DeliveryNotification();
        deliveryNotification.setNotificationMessage("test message");

        try {
            System.out.println("requesttoPayDeliveryNotification");
            statusResponse = disbursementRequest.requestToPayDeliveryNotification(referenceId, deliveryNotification);
            System.out.println("statusResponse : " + JSONFormatter.toJSON(statusResponse));
        } catch (MoMoException e) {
            //409 : Conflict        46733123450, 1, 2, 3,... //"code": "NOT_SUCCESSFULLY_COMPLETED" //"message": "The transaction is not successfully completed."
            Logger.getLogger(MoMoApiSDK.class.getName()).log(Level.SEVERE, null, e);
        }

        System.out.println("requestToPay");
        statusResponse = collectionRequest.requestToPay(requestPay);
        System.out.println("statusResponse : " + JSONFormatter.toJSON(statusResponse));

//        String referenceId = "18566e1d-971f-4564-a7ec-6f10d7c87e4e";
        referenceId = statusResponse.getReferenceId();

        try {
            System.out.println("requesttoPayDeliveryNotification");
            statusResponse = remittanceRequest.requestToPayDeliveryNotification(referenceId, deliveryNotification);
            System.out.println("statusResponse : " + JSONFormatter.toJSON(statusResponse));
        } catch (MoMoException e) {
            //409 : Conflict        46733123450, 1, 2, 3,... //"code": "NOT_SUCCESSFULLY_COMPLETED" //"message": "The transaction is not successfully completed."
            Logger.getLogger(MoMoApiSDK.class.getName()).log(Level.SEVERE, null, e);
        }

        transfer = getTransfer(MSISDN);

        System.out.println("transfer");
        statusResponse = disbursementRequest.transfer(transfer);
        System.out.println("statusResponse : " + JSONFormatter.toJSON(statusResponse));

        System.out.println("requestToPay");
        statusResponse = collectionRequest.requestToPay(requestPay);
        System.out.println("statusResponse : " + JSONFormatter.toJSON(statusResponse));

    }

    private static void getAccessToken(String MSISDN, String SUBSCRIPTION_KEY_VALUE) throws MoMoException {
        System.out.println(":::::::::MSISDN: " + MSISDN);

        switch (SUBSCRIPTION_KEY_VALUE) {
            case COLLECTION_SUBSCRIPTION_KEY:
                System.out.println("SUBSCRIPTION_KEY_VALUE:- " + SUBSCRIPTION_KEY_VALUE);
                CollectionConfiguration collectionConfiguration = new CollectionConfiguration(COLLECTION_SUBSCRIPTION_KEY, REFERENCE_ID, API_KEY, Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(CALLBACK_URL);
                CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

                System.out.println("CollectionAccessToken: " + CollectionContext.getContext().fetchAccessToken().getAccess_token());
//                AccountHolder accountHolder1 = new AccountHolder(IdType.MSISDN.getValue(), MSISDN);
                AccountHolder accountHolder1 = new AccountHolder(IdType.MSISDN.getValue(), "test");
                UserInfo userInfo1 = collectionRequest.getUserInfoWithConsent(accountHolder1, "profile", AccessType.OFFLINE);
                System.out.println("userInfo: " + JSONFormatter.toJSON(userInfo1));
                break;
            case DISBURSEMENT_SUBSCRIPTION_KEY:
                System.out.println("SUBSCRIPTION_KEY_VALUE:- " + SUBSCRIPTION_KEY_VALUE);
                DisbursementConfiguration disbursementConfiguration = new DisbursementConfiguration(DISBURSEMENT_SUBSCRIPTION_KEY, REFERENCE_ID, API_KEY).addCallBackUrl(CALLBACK_URL);
                DisbursementRequest disbursementRequest = disbursementConfiguration.createDisbursementRequest();

                System.out.println("DisbursementAccessToken: " + DisbursementContext.getContext().fetchAccessToken().getAccess_token());
                AccountHolder accountHolder2 = new AccountHolder(IdType.MSISDN.getValue(), "23423423451");
//                UserInfo userInfo2 = disbursementRequest.getUserInfoWithConsent(accountHolder2, "profile", AccessType.OFFLINE);
//                System.out.println("userInfo: " + JSONFormatter.toJSON(userInfo2));
                break;
            case REMITTANCE_SUBSCRIPTION_KEY:
                System.out.println("SUBSCRIPTION_KEY_VALUE:- " + SUBSCRIPTION_KEY_VALUE);
                RemittanceConfiguration remittanceConfiguration = new RemittanceConfiguration(REMITTANCE_SUBSCRIPTION_KEY, REFERENCE_ID, API_KEY).addCallBackUrl(CALLBACK_URL);
                RemittanceRequest remittanceRequest = remittanceConfiguration.createRemittanceRequest();

                System.out.println("RemittanceAccessToken: " + RemittanceContext.getContext().fetchAccessToken().getAccess_token());
                AccountHolder accountHolder3 = new AccountHolder(IdType.MSISDN.getValue(), "23423423452");
//                UserInfo userInfo3 = remittanceRequest.getUserInfoWithConsent(accountHolder3, "profile", AccessType.OFFLINE);
//                System.out.println("userInfo: " + JSONFormatter.toJSON(userInfo3));
                break;
            default:
                throw new AssertionError();
        }

    }

    private static void collection(String MSISDN) throws MoMoException {
        System.out.println(":::::::::MSISDN: " + MSISDN);

        CollectionConfiguration collectionConfiguration = new CollectionConfiguration(COLLECTION_SUBSCRIPTION_KEY, REFERENCE_ID, API_KEY, Environment.SANDBOX, Constants.SANDBOX).addCallBackUrl(CALLBACK_URL);
        CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

        System.out.println("CollectionAccessToken: " + CollectionContext.getContext().fetchAccessToken().getAccess_token());

        ////************
//        System.exit(0);
        System.out.println("requestToPay");
        RequestPay requestPay = getPay(MSISDN);
        StatusResponse statusResponse = collectionRequest.requestToPay(requestPay);
        System.out.println("statusResponse : " + JSONFormatter.toJSON(statusResponse));
        requestToPayReferenceId = statusResponse.getReferenceId();
        //Tested requestToPay

        System.out.println("requesttoPayTransactionStatus");
        RequestPayStatus requestPayStatus = collectionRequest.requestToPayTransactionStatus(statusResponse.getReferenceId());
        System.out.println("payStatus : " + JSONFormatter.toJSON(requestPayStatus));
        //Tested

        DeliveryNotification deliveryNotification = new DeliveryNotification();
        deliveryNotification.setNotificationMessage("test message");

        try {
            System.out.println("requesttoPayDeliveryNotification");
            statusResponse = collectionRequest.requestToPayDeliveryNotification(statusResponse.getReferenceId(), deliveryNotification);
            System.out.println("statusResponse : " + JSONFormatter.toJSON(statusResponse));
        } catch (MoMoException e) {
            //409 : Conflict        46733123450, 1, 2, 3,... //"code": "NOT_SUCCESSFULLY_COMPLETED" //"message": "The transaction is not successfully completed."
            Logger.getLogger(MoMoApiSDK.class.getName()).log(Level.SEVERE, null, e);
        }

        AccountHolder accountHolder = new AccountHolder(IdType.MSISDN.getValueInLowerCase(), MSISDN);

        try {
            System.out.println("getUserInfoWithConsent");
            UserInfo userInfo = collectionRequest.getUserInfoWithConsent(accountHolder, "profile", AccessType.OFFLINE);
            System.out.println("userInfo: " + JSONFormatter.toJSON(userInfo));
        } catch (MoMoException e) {
            Logger.getLogger(MoMoApiSDK.class.getName()).log(Level.SEVERE, null, e);
        }
        
        Result result;
        try {
            System.out.println("validateAccountHolderStatus");
            result = collectionRequest.validateAccountHolderStatus(accountHolder);
            System.out.println("result : " + JSONFormatter.toJSON(result));
        } catch (MoMoException e) {
            //404 : Not Found       46733123450 //"code": "RESOURCE_NOT_FOUND" //"message": "Requested resource was not found."
            //500 : Server Error    46733123452 //"code": "NOT_ALLOWED" //"message": "Authorization failed. Insufficient permissions."
            //500 : Server Error    46733123453 //"code": "NOT_ALLOWED_TARGET_ENVIRONMENT" //"message": "Access to target environment is forbidden."
            //500 : Server Error    46733123454 //"code": "INTERNAL_PROCESSING_ERROR" //"message": "An internal error occurred while processing.
            //503 : Service Unavailable    46733123455 //"code": "SERVICE_UNAVAILABLE" //"message": "Service temporarily unavailable, try again later."
            Logger.getLogger(MoMoApiSDK.class.getName()).log(Level.SEVERE, null, e);
        }

        AccountBalance accountBalance = null;
        try {//working 46733123451:0, 46733123454:-25, ...
            System.out.println("getAccountBalance");
            accountBalance = collectionRequest.getAccountBalance();
            System.out.println("accountBalance : " + JSONFormatter.toJSON(accountBalance));
        } catch (MoMoException e) {
            //500 : Server Error    46733123450 //"code": "NOT_ALLOWED" //"message": "Authorization failed. Insufficient permissions."
            //500 : Server Error    46733123452 //"code": "INTERNAL_PROCESSING_ERROR" //"message": "An internal error occurred while processing.
            //503 : Service Unavailable    46733123453, 56, 57 //"code": "SERVICE_UNAVAILABLE" //"message": "Service temporarily unavailable, try again later.
            //500 : Server Error    46733123455 //"code": "NOT_ALLOWED_TARGET_ENVIRONMENT" //"message": "Access to target environment is forbidden."
            Logger.getLogger(MoMoApiSDK.class.getName()).log(Level.SEVERE, null, e);
        }

        try {
            String currency = accountBalance != null ? accountBalance.getCurrency() : "EUR";
            System.out.println("getAccountBalanceInSpecificCurrency");
            accountBalance = collectionRequest.getAccountBalanceInSpecificCurrency(currency);
            System.out.println("accountBalance : " + JSONFormatter.toJSON(accountBalance));
        } catch (MoMoException e) {
            //500 : Server Error
            Logger.getLogger(MoMoApiSDK.class.getName()).log(Level.SEVERE, null, e);
        }
        //500 : Server Error

        try {
            Withdraw withdraw = getWithdraw(MSISDN);

            System.out.println("requestToWithdrawV1");
            statusResponse = collectionRequest.requestToWithdrawV1(withdraw);
            System.out.println("statusResponse : " + JSONFormatter.toJSON(statusResponse));
        } catch (MoMoException e) {
            Logger.getLogger(MoMoApiSDK.class.getName()).log(Level.SEVERE, null, e);
        }
        try {
            System.out.println("requestToWithdrawTransactionStatus");
            WithdrawStatus withdrawStatus = collectionRequest.requestToWithdrawTransactionStatus(statusResponse.getReferenceId());
            System.out.println("withdrawStatus : " + JSONFormatter.toJSON(withdrawStatus));
        } catch (MoMoException e) {
            Logger.getLogger(MoMoApiSDK.class.getName()).log(Level.SEVERE, null, e);
        }
        try {
            Withdraw withdraw = getWithdraw(MSISDN);

            System.out.println("requestToWithdrawV2");
            statusResponse = collectionRequest.requestToWithdrawV2(withdraw);
            System.out.println("statusResponse : " + JSONFormatter.toJSON(statusResponse));
        } catch (MoMoException e) {
            Logger.getLogger(MoMoApiSDK.class.getName()).log(Level.SEVERE, null, e);
        }
        try {
            System.out.println("requestToWithdrawTransactionStatus");
            WithdrawStatus withdrawStatus = collectionRequest.requestToWithdrawTransactionStatus(statusResponse.getReferenceId());
            System.out.println("withdrawStatus : " + JSONFormatter.toJSON(withdrawStatus));
        } catch (MoMoException e) {
            Logger.getLogger(MoMoApiSDK.class.getName()).log(Level.SEVERE, null, e);
        }

        try {
            System.out.println("getBasicUserinfo");
            BasicUserInfo basicUserInfo = collectionRequest.getBasicUserinfo(MSISDN);
            System.out.println("basicUserInfo : " + JSONFormatter.toJSON(basicUserInfo));
        } catch (MoMoException e) {
            Logger.getLogger(MoMoApiSDK.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    private static void disbursement(String MSISDN, String requestToPayReferenceID) throws MoMoException {
        //referenceId: b051eabc-64cf-467b-acab-10bd1407e5ea
        //ApiKey: 292ba3c3a4cf4de5b9b726b2e1186f43
        System.out.println(":::::::::MSISDN: " + MSISDN);
        StatusResponse statusResponse = null;

        Transfer transfer = getTransfer(MSISDN);

        DisbursementConfiguration disbursementConfiguration = new DisbursementConfiguration(DISBURSEMENT_SUBSCRIPTION_KEY, REFERENCE_ID, API_KEY).addCallBackUrl(CALLBACK_URL);
        DisbursementRequest disbursementRequest = disbursementConfiguration.createDisbursementRequest();
        System.out.println("DisbursementAccessToken: " + DisbursementContext.getContext().fetchAccessToken().getAccess_token());
System.exit(0);

        ////************
//        System.exit(0);
        System.out.println("transfer");
        statusResponse = disbursementRequest.transfer(transfer);
        System.out.println("statusResponse : " + JSONFormatter.toJSON(statusResponse));

        System.out.println("getTransferStatus");
        TransferStatus transferStatus = disbursementRequest.getTransferStatus(statusResponse.getReferenceId());
        System.out.println("transferStatus : " + JSONFormatter.toJSON(transferStatus));

        AccountHolder accountHolder = new AccountHolder(IdType.MSISDN.getValueInLowerCase(), MSISDN);

        Result result;
        try {
            System.out.println("validateAccountHolderStatus");
            result = disbursementRequest.validateAccountHolderStatus(accountHolder);
            System.out.println("result : " + JSONFormatter.toJSON(result));
        } catch (MoMoException e) {
            //404 : Not Found       46733123450 //"code": "RESOURCE_NOT_FOUND" //"message": "Requested resource was not found."
            //500 : Server Error    46733123452 //"code": "NOT_ALLOWED" //"message": "Authorization failed. Insufficient permissions."
            //500 : Server Error    46733123453 //"code": "NOT_ALLOWED_TARGET_ENVIRONMENT" //"message": "Access to target environment is forbidden."
            //500 : Server Error    46733123454 //"code": "INTERNAL_PROCESSING_ERROR" //"message": "An internal error occurred while processing.
            //503 : Service Unavailable    46733123455 //"code": "SERVICE_UNAVAILABLE" //"message": "Service temporarily unavailable, try again later."
            Logger.getLogger(MoMoApiSDK.class.getName()).log(Level.SEVERE, null, e);
        }

        AccountBalance accountBalance = null;
        try {//working 46733123451:0, 46733123454:-25, ...
            System.out.println("getAccountBalance");
            accountBalance = disbursementRequest.getAccountBalance();
            System.out.println("accountBalance : " + JSONFormatter.toJSON(accountBalance));
        } catch (MoMoException e) {
            //500 : Server Error    46733123450 //"code": "NOT_ALLOWED" //"message": "Authorization failed. Insufficient permissions."
            //500 : Server Error    46733123452 //"code": "INTERNAL_PROCESSING_ERROR" //"message": "An internal error occurred while processing.
            //503 : Service Unavailable    46733123453, 56, 57 //"code": "SERVICE_UNAVAILABLE" //"message": "Service temporarily unavailable, try again later.
            //500 : Server Error    46733123455 //"code": "NOT_ALLOWED_TARGET_ENVIRONMENT" //"message": "Access to target environment is forbidden."
            Logger.getLogger(MoMoApiSDK.class.getName()).log(Level.SEVERE, null, e);
        }

        try {
            String currency = accountBalance != null ? accountBalance.getCurrency() : "EUR";
            System.out.println("getAccountBalanceInSpecificCurrency");
            accountBalance = disbursementRequest.getAccountBalanceInSpecificCurrency(currency);
            System.out.println("accountBalance : " + JSONFormatter.toJSON(accountBalance));
        } catch (MoMoException e) {
            //500 : Server Error
            Logger.getLogger(MoMoApiSDK.class.getName()).log(Level.SEVERE, null, e);
        }

        try {
            Deposit deposit = getDeposit(MSISDN, "V1");

            System.out.println("depositV1");
            System.out.println("======================= check callback");
            statusResponse = disbursementRequest.depositV1(deposit);
            System.out.println("statusResponse : " + JSONFormatter.toJSON(statusResponse));
        } catch (MoMoException e) {
            Logger.getLogger(MoMoApiSDK.class.getName()).log(Level.SEVERE, null, e);
        }
        try {
            System.out.println("getDepositStatus");
            DepositStatus depositStatus = disbursementRequest.getDepositStatus(statusResponse.getReferenceId());
            System.out.println("depositStatus : " + JSONFormatter.toJSON(depositStatus));
        } catch (MoMoException e) {
            Logger.getLogger(MoMoApiSDK.class.getName()).log(Level.SEVERE, null, e);
        }
        try {
            Deposit deposit = getDeposit(MSISDN, "V2");

            System.out.println("depositV2");
            statusResponse = disbursementRequest.depositV2(deposit);
            System.out.println("statusResponse : " + JSONFormatter.toJSON(statusResponse));
        } catch (MoMoException e) {
            Logger.getLogger(MoMoApiSDK.class.getName()).log(Level.SEVERE, null, e);
        }
        try {
            System.out.println("getDepositStatus");
            DepositStatus depositStatus = disbursementRequest.getDepositStatus(statusResponse.getReferenceId());
            System.out.println("depositStatus : " + JSONFormatter.toJSON(depositStatus));
        } catch (MoMoException e) {
            Logger.getLogger(MoMoApiSDK.class.getName()).log(Level.SEVERE, null, e);
        }

        try {
            //referenceId from request to pay of collection
            Refund refund = getRefund(requestToPayReferenceID);

            System.out.println("refundV1");
            statusResponse = disbursementRequest.refundV1(refund);
            System.out.println("statusResponse : " + JSONFormatter.toJSON(statusResponse));
        } catch (MoMoException e) {
            Logger.getLogger(MoMoApiSDK.class.getName()).log(Level.SEVERE, null, e);
        }

        try {
            System.out.println("getRefundStatus");
            RefundStatus refundStatus = disbursementRequest.getRefundStatus(statusResponse.getReferenceId());
            System.out.println("refundStatus : " + JSONFormatter.toJSON(refundStatus));
        } catch (MoMoException e) {
            Logger.getLogger(MoMoApiSDK.class.getName()).log(Level.SEVERE, null, e);
        }
        try {
            //referenceId from request to pay of collection
            Refund refund = getRefund(requestToPayReferenceID);

            System.out.println("refundV2");
            statusResponse = disbursementRequest.refundV2(refund);
            System.out.println("statusResponse : " + JSONFormatter.toJSON(statusResponse));
        } catch (MoMoException e) {
            Logger.getLogger(MoMoApiSDK.class.getName()).log(Level.SEVERE, null, e);
        }
        try {
            System.out.println("getRefundStatus");
            RefundStatus refundStatus = disbursementRequest.getRefundStatus(statusResponse.getReferenceId());
            System.out.println("refundStatus : " + JSONFormatter.toJSON(refundStatus));
        } catch (MoMoException e) {
            Logger.getLogger(MoMoApiSDK.class.getName()).log(Level.SEVERE, null, e);
        }

        try {
            System.out.println("getBasicUserinfo");
            BasicUserInfo basicUserInfo = disbursementRequest.getBasicUserinfo(MSISDN);
            System.out.println("basicUserInfo : " + JSONFormatter.toJSON(basicUserInfo));
        } catch (MoMoException e) {
            Logger.getLogger(MoMoApiSDK.class.getName()).log(Level.SEVERE, null, e);
        }
        DeliveryNotification deliveryNotification = new DeliveryNotification();
        deliveryNotification.setNotificationMessage("test message");

        try {
            System.out.println("requesttoPayDeliveryNotification");
            statusResponse = disbursementRequest.requestToPayDeliveryNotification(requestToPayReferenceID, deliveryNotification);
            System.out.println("statusResponse : " + JSONFormatter.toJSON(statusResponse));
        } catch (MoMoException e) {
            //409 : Conflict        46733123450, 1, 2, 3,... //"code": "NOT_SUCCESSFULLY_COMPLETED" //"message": "The transaction is not successfully completed."
            Logger.getLogger(MoMoApiSDK.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    private static void remittance(String MSISDN, String requestToPayReferenceID) throws MoMoException {
        System.out.println(":::::::::MSISDN: " + MSISDN);
        StatusResponse statusResponse = null;

        Transfer transfer = getTransfer(MSISDN);

        RemittanceConfiguration mmClient2 = new RemittanceConfiguration(REMITTANCE_SUBSCRIPTION_KEY, REFERENCE_ID, API_KEY).addCallBackUrl(CALLBACK_URL);
        RemittanceRequest remittanceRequest = mmClient2.createRemittanceRequest();

        System.out.println("RemittanceAccessToken: " + RemittanceContext.getContext().fetchAccessToken().getAccess_token());

        ////************
//        System.exit(0);
        System.out.println("transfer");
        statusResponse = remittanceRequest.transfer(transfer);
        System.out.println("statusResponse : " + JSONFormatter.toJSON(statusResponse));

        System.out.println("getTransferStatus");
        TransferStatus transferStatus = remittanceRequest.getTransferStatus(statusResponse.getReferenceId());
        System.out.println("transferStatus : " + JSONFormatter.toJSON(transferStatus));

        AccountHolder accountHolder = new AccountHolder(IdType.MSISDN.getValueInLowerCase(), MSISDN);

        Result result;
        try {
            System.out.println("validateAccountHolderStatus");
            result = remittanceRequest.validateAccountHolderStatus(accountHolder);
            System.out.println("result : " + JSONFormatter.toJSON(result));
        } catch (MoMoException e) {
            //404 : Not Found       46733123450 //"code": "RESOURCE_NOT_FOUND" //"message": "Requested resource was not found."
            //500 : Server Error    46733123452 //"code": "NOT_ALLOWED" //"message": "Authorization failed. Insufficient permissions."
            //500 : Server Error    46733123453 //"code": "NOT_ALLOWED_TARGET_ENVIRONMENT" //"message": "Access to target environment is forbidden."
            //500 : Server Error    46733123454 //"code": "INTERNAL_PROCESSING_ERROR" //"message": "An internal error occurred while processing.
            //503 : Service Unavailable    46733123455 //"code": "SERVICE_UNAVAILABLE" //"message": "Service temporarily unavailable, try again later."
            Logger.getLogger(MoMoApiSDK.class.getName()).log(Level.SEVERE, null, e);
        }

        AccountBalance accountBalance = null;
        try {//working 46733123451:0, 46733123454:-25, ...
            System.out.println("getAccountBalance");
            accountBalance = remittanceRequest.getAccountBalance();
            System.out.println("accountBalance : " + JSONFormatter.toJSON(accountBalance));
        } catch (MoMoException e) {
            //500 : Server Error    46733123450 //"code": "NOT_ALLOWED" //"message": "Authorization failed. Insufficient permissions."
            //500 : Server Error    46733123452 //"code": "INTERNAL_PROCESSING_ERROR" //"message": "An internal error occurred while processing.
            //503 : Service Unavailable    46733123453, 56, 57 //"code": "SERVICE_UNAVAILABLE" //"message": "Service temporarily unavailable, try again later.
            //500 : Server Error    46733123455 //"code": "NOT_ALLOWED_TARGET_ENVIRONMENT" //"message": "Access to target environment is forbidden."
            Logger.getLogger(MoMoApiSDK.class.getName()).log(Level.SEVERE, null, e);
        }

//        try {
//            String currency = accountBalance != null ? accountBalance.getCurrency() : "EUR";
//            System.out.println("getAccountBalanceInSpecificCurrency");
//            accountBalance = remittanceRequest.getAccountBalanceInSpecificCurrency(currency);
//            System.out.println("accountBalance : " + JSONFormatter.toJSON(accountBalance));
//        } catch (MoMoException e) {
//            //404 : Resource Not Found //"statusCode": "404" "message": "Resource not found"
//            Logger.getLogger(MoMoApiSDK.class.getName()).log(Level.SEVERE, null, e);
//        }
        try {
            System.out.println("getBasicUserinfo");
            BasicUserInfo basicUserInfo = remittanceRequest.getBasicUserinfo(MSISDN);
            System.out.println("basicUserInfo : " + JSONFormatter.toJSON(basicUserInfo));
        } catch (MoMoException e) {
            Logger.getLogger(MoMoApiSDK.class.getName()).log(Level.SEVERE, null, e);
        }
        DeliveryNotification deliveryNotification = new DeliveryNotification();
        deliveryNotification.setNotificationMessage("test message");

        try {
            System.out.println("requesttoPayDeliveryNotification");
            statusResponse = remittanceRequest.requestToPayDeliveryNotification(requestToPayReferenceID, deliveryNotification);
            System.out.println("statusResponse : " + JSONFormatter.toJSON(statusResponse));
        } catch (MoMoException e) {
            //409 : Conflict        46733123450, 1, 2, 3,... //"code": "NOT_SUCCESSFULLY_COMPLETED" //"message": "The transaction is not successfully completed."
            Logger.getLogger(MoMoApiSDK.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private static RequestPay getPay(String MSISDN) {
        Payer payer = new Payer();
        payer.setPartyId(MSISDN);
        payer.setPartyIdType(IdType.MSISDN.getValue());

        RequestPay requestPay = new RequestPay();
        requestPay.setAmount("6.0");
        requestPay.setCurrency("EUR");
        requestPay.setExternalId("6353636");
        requestPay.setPayeeNote("payer note");
        requestPay.setPayerMessage("Pay for product a");
        requestPay.setPayer(payer);
        return requestPay;
    }

    private static Withdraw getWithdraw(String MSISDN) {
        Payer payer = new Payer();
        payer.setPartyId(MSISDN);
        payer.setPartyIdType(IdType.MSISDN.getValue());

        Withdraw withdraw = new Withdraw();
        withdraw.setAmount("6.0");
        withdraw.setCurrency("EUR");
        withdraw.setExternalId("6353636");
        withdraw.setPayeeNote("payer note");
        withdraw.setPayerMessage("Pay for product a");
        withdraw.setPayer(payer);
        return withdraw;
    }

    private static Transfer getTransfer(String MSISDN) {
        Payee payee = new Payee();
        payee.setPartyId(MSISDN);
        payee.setPartyIdType(IdType.MSISDN.getValue());

        Transfer transfer = new Transfer();
        transfer.setAmount("6.0");
        transfer.setCurrency("EUR");
        transfer.setExternalId("6353636");
        transfer.setPayeeNote("payer note");
        transfer.setPayerMessage("Pay for product a");
        transfer.setPayee(payee);
        return transfer;
    }

    private static Deposit getDeposit(String MSISDN, String version) {
        Payee payee = new Payee();
        payee.setPartyId(MSISDN);
        payee.setPartyIdType(IdType.MSISDN.getValue());

        Deposit deposit = new Deposit();
        deposit.setAmount("6.0");
        deposit.setCurrency("EUR");
        deposit.setExternalId("6353636");
        deposit.setPayeeNote("payer note deposit: " + version);
        deposit.setPayerMessage("Pay for product a  deposit: " + version);
        deposit.setPayee(payee);
        return deposit;
    }

    private static Refund getRefund(String referenceId) {
        Refund refund = new Refund();
        refund.setAmount("6.0");
        refund.setCurrency("EUR");
        refund.setExternalId("6353636");
        refund.setPayeeNote("payer note");
        refund.setPayerMessage("Pay for product a");
        refund.setReferenceIdToRefund(referenceId);
        return refund;
    }

    /**
     * *
     * Returns Base64 string generated after concatenating
     * referenceId(ApiUser/UUID) and apiKey
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    private static String generateBase64String(String referenceId, String apiKey) throws UnsupportedEncodingException {
        String base64ClientID;
        byte[] encoded;
        try {
            encoded = Base64.getEncoder().encode((referenceId + ":" + apiKey).getBytes());
            base64ClientID = new String(encoded);
        } catch (Exception e) {
            throw new UnsupportedEncodingException(e.getMessage());
        }
        return base64ClientID;
    }
}
