package com.momo.api.base.constants;

public final class Constants {

    //Maximum retry value
    public static final int MAX_RETRIES = 3;

    //Encoding format
    public static final String ENCODING_FORMAT = "UTF-8";

    //Mode(sandbox/production)
    public static final String MODE = "mode";

    public static final String SANDBOX = "sandbox";

    //REST API resource endpoint
    public static final String ENDPOINT = "resource.endpoint";

    //X-Reference-Id from API User 
    public static final String API_USER_REFERENCE_ID = "{X-Reference-Id}";

    //Subscription type(collection/disbursement/remittance)
    public static final String SUBSCRIPTION_TYPE = "{subscriptionType}";

    //Request Type(requestToPay/requesttowithdraw/transfer/deposit/refund)
    public static final String REQUEST_TYPE = "{requestType}";

    //referenceId(or UUID)
    public static final String REFERENCE_ID = "{referenceId}";

    //Currency(Eg:- EUR)
    public static final String CURRENCY = "{currency}";

    // Invalid REST endpoint
    public static final String INVALID_REST_ENDPOINT = "REST API endpoint could not be fetched properly.";

    // Authorization Header
    public static final String AUTHORIZATION_HEADER = "Authorization";

    // Bearer
    public static final String BEARER = "Bearer ";

    // Basic
    public static final String BASIC = "Basic ";

    // Call Back URL
    public static final String CALL_BACK_URL = "X-Callback-Url";

    // Http override method
    public static final String HTTP_OVERRIDE_METHOD = "X-HTTP-Method-Override";

    //X-Reference-Id for headers
    public static final String X_REFERENCE_ID = "X-Reference-Id";

    //If there is a set of values available, we can have a class containing them as String or ENUM
    public static final String TARGET_ENVIRONMENT = "X-Target-Environment";

    // Subscription Key
    public static final String SUBSCRIPTION_KEY = "Ocp-Apim-Subscription-Key";

    //Delivery Notification Header
    public static final String NOTIFICATION_MESSAGE = "notificationMessage";

    //Account Holder Type
    public static final String ACCOUNT_HOLDER_ID_TYPE = "{accountHolderIdType}";

    //Account Holder Unique ID value
    public static final String ACCOUNT_HOLDER_ID = "{accountHolderId}";

    // Default SDK configuration file name
    public static final String DEFAULT_CONFIGURATION_FILE = "sdk_config.properties";

    // SSLUtil Protocol
    public static final String SSLUTIL_PROTOCOL = "sslutil.protocol";

    // Access Token
    public static final String ACCESS_TOKEN = "access_token";

    // Api Key
    public static final String API_KEY = "apiKey";

    // Invalid access token message
    public static final String EMPTY_ACCESS_TOKEN_MESSAGE = "AccessToken cannot be null or empty";

    // HTTP Method Default
    public static final String HTTP_CONFIG_DEFAULT_HTTP_METHOD = "POST";

    // OAuth End point
    public static final String OAUTH_ENDPOINT = "oauth.EndPoint";

    // HTTP Error Buffering
    public static final String ENABLE_HTTP_ERROR_BUFFERING = "sun.net.http.errorstream.enableBuffering";

    // HTTP Max Connections
    public static final String HTTP_CONNECTION_MAX_CONNECTION = "http.MaxConnection";

    // HTTP Content Type JSON
    public static final String HTTP_CONTENT_TYPE_JSON = "application/json";

    // HTTP Content-Type Header
    public static final String HTTP_CONTENT_TYPE_HEADER = "Content-Type";

    // HTTP Content-Length Header
    public static final String HTTP_CONTENT_LENGTH = "Content-Length";

    // Validation Error Category
    public static final String VALIDATION_ERROR_CATEGORY = "Validation";

    // Internal Error Category
    public static final String INTERNAL_ERROR_CATEGORY = "Internal";

    // Mandatory Value Not Supplied
    public static final String VALUE_NOT_SUPPLIED_ERROR_CODE = "MandatoryValueNotSupplied";

    // Generic Error
    public static final String GENERIC_ERROR_CODE = "GenericError";

    // Invalid Format
    public static final String INVALID_FORMAT_CODE = "InvalidFormat";

    // General Error
    public static final String GENRAL_ERROR = "The request could not be completed";

    // Null Value
    public static final String NULL_VALUE_ERROR = "The request could not be processed due to NULL value";

    // Empty string Value
    public static final String EMPTY_STRING_ERROR = "The request could not be processed due to Empty String";

    // Empty or Null String
    public static final String NULL_ENVIRONMENT_ERROR = "Environment must not be null";

    // Invalid parameters
    public static final String INVALID_PARAMETERS = "The request could not be processed due to invalid parameters";

    // Request not created
    public static final String REQUEST_NOT_CREATED = "Make sure the corresponding request object is created with valid parameters before making API calls";

    // Pay Object Is Null
    public static final String PAY_OBJECT_INIT_ERROR = "Pay object is not initialized";

    // Transfer Object Is Null
    public static final String TRANSFER_OBJECT_INIT_ERROR = "Transfer object is not initialized";

    // Deposit Object Is Null
    public static final String DEPOSIT_OBJECT_INIT_ERROR = "Deposit object is not initialized";

    // Refund Object Is Null
    public static final String REFUND_OBJECT_INIT_ERROR = "Refund object is not initialized";

    // Withdraw Object Is Null
    public static final String WITHDRAW_OBJECT_INIT_ERROR = "Withdraw object is not initialized";

    // Pay Object Is Null
    public static final String ACCOUNT_HOLDER_OBJECT_INIT_ERROR = "AccountHolder object is not initialized";

    // DeliveryNotification Object Is Null
    public static final String DELIVERY_NOTIFICATION_OBJECT_INIT_ERROR = "DeliveryNotification object is not initialized";

    // ApiUser Object Is Null
    public static final String API_USER_OBJECT_INIT_ERROR = "ApiUser object is not initialized";

    // CallbackHost Object Is Null
    public static final String CALL_BACK_HOST_OBJECT_INIT_ERROR = "CallbackHost object is not initialized";

    // Invalid Callback URL format
    public static final String INVALID_CALLBACK_URL_FORMAT_ERROR = "Invalid Callback URL format";
}
