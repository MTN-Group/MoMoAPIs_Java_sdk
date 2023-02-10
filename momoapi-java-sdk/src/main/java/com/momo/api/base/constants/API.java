package com.momo.api.base.constants;

public final class API {

    private API() {
    }

    //Base URL for Sandbox Environment
    public static final String SANDBOX_URL = "https://sandbox.momodeveloper.mtn.com";

    //TODO do we need to make the production url modifiable in some way?
    //Base URL for Production Environment
    public static final String PRODUCTION_URL = "";

    //Create an API user
    public static final String API_USER = "/v1_0/apiuser";

    //Get an API user
    public static final String X_REFERENCE_ID = "/{X-Reference-Id}";

    //Create an API key
    public static final String API_KEY = "/{X-Reference-Id}/apikey";

    //Create an access token
    public static final String SUBSCRIPTION_TOKEN = "/{subscriptionType}/token/";

    //To make a Request
    public static final String SUBSCRIPTION_VER_REQUEST = "/{subscriptionType}/v1_0/{requestType}";

    //To make a Request with v2_0 url
    public static final String SUBSCRIPTION_VER2_REQUEST = "/{subscriptionType}/v2_0/{requestType}";

    //Get status of a request
    public static final String SUBSCRIPTION_VER_REQUEST_REFERENCE_ID = SUBSCRIPTION_VER_REQUEST + "/{referenceId}";

    //Send delivery notification
    public static final String SUBSCRIPTION_VER_REQUEST_REFERENCE_ID_DELIVERYNOTIFICATION = SUBSCRIPTION_VER_REQUEST_REFERENCE_ID + "/deliverynotification";

    //Base URL for account holder informations
    public static final String SUBSCRIPTION_VER_ACCOUNTHOLDER = "/{subscriptionType}/v1_0/accountholder/{accountHolderIdType}/{accountHolderId}";

    //Check if an account holder is registered
    public static final String SUBSCRIPTION_VER_ACCOUNTHOLDER_STATUS = SUBSCRIPTION_VER_ACCOUNTHOLDER + "/active";

    //Get personal information of the account holder
    public static final String SUBSCRIPTION_VER_ACCOUNTHOLDER_USER_INFO = SUBSCRIPTION_VER_ACCOUNTHOLDER + "/basicuserinfo";

    //Get the balance of the account
    public static final String SUBSCRIPTION_VER_ACCOUNT_BALANCE = "/{subscriptionType}/v1_0/account/balance";

    //Get the balance of the account for a currency
    public static final String SUBSCRIPTION_VER_ACCOUNT_BALANCE_CURRENCY = SUBSCRIPTION_VER_ACCOUNT_BALANCE + "/{currency}";
}
