# MTN MoMo API - SDK for Java

This SDK provides for an easy way to connect to [MTN MoMo API](https://momodeveloper.mtn.com/api-documentation).

Please refer to the following documentation for installation instructions and usage information.

-   [API Documentation](https://momodeveloper.mtn.com/api-documentation)
-   [Java SDK Documentation](docs/)
-   [Java SDK Sample Codes](momoapi-java-sdk-samples/src/)

## Index

This document contains the following sections:

-  [Requirements](#requirements)
-  [Getting Started](#getting-started)
     -  [Installation](#installation)
     -  [Development and Testing](#development-and-testing)
-  [Setting Up](#setting-up)
-  [Handling Errors](#handling-errors)
-  [Use Cases](#use-cases)
    -   [Sandbox User Provisioning](#sandbox)
    -   [Collection](#collection)
    -   [Disbursement](#disbursement)
    -   [Remittance](#remittance)
-  [Testing](#testing)
-  [Samples](#samples)

## Requirements

-   Java JDK-1.8 or higher
-   Apache Maven 3 or higher

## Getting Started

### Installation

1. Build the jar file with tests using  'mvn clean package' command or without running tests using 'mvn clean package -Dmaven.test.skip=true' 
2. Import 'momoapi-java-sdk' jar file to your project's classpath

In order to build the SDK from the source code you need to use Apache Maven and Java 1.8+

### Development and Testing

1. Tests for the SDK are in the src/test/java package.
2. Rename `config.properties.sample` file in `src\test\resources` to `config.properties` and replace placeholders with values for your `reference id` (also known as `api user` ) , `api key` and for each of the  `subscription keys`.
3. From the test package, run JUnit test for each test classes

## Setting Up

### Initialization of Java SDK

All Java code snippets are listed [here](/docs). Assumes that you have initialized the Java SDK before using them in your Development Environment. 
This section details the initialization of the Java SDK.

To initialize the Java SDK, we need to create an instance for the corresponding subscription.

When developing in the `sandbox` environment, [`Sandbox User Provisioning`](https://momodeveloper.mtn.com/docs/services/sandbox-provisioning-api/operations/post-v1_0-apiuser) need to be completed to get an `API User`(also known as referenceId) and `API Key`, which are later used in a subscription for making requests. 

Following is the code for initialising the `UserProvisioningConfiguration` which can be used for creating `UserProvisioningRequest`.

```java
UserProvisioningConfiguration userProvisioningConfiguration = new UserProvisioningConfiguration("<place your SUBSCRIPTION_KEY>");
```
In the `production` environment the provisioning is done through the Merchant Portal.

For Collection create an instance of `CollectionConfiguration` with the respective `subscription key`, `reference id` (also known as `api user` ) and `apiKey`. These are the default parameters and will point to `sandbox environement`. Parameters required for `production environement` are discussed in a later steps.

```java
CollectionConfiguration collectionConfiguration = new CollectionConfiguration("<place your COLLECTION_SUBSCRIPTION_KEY>", "<place your REFERENCE_ID>", "<place your API_KEY>");
```

For Disbursement create an instance of `DisbursementConfiguration`.

```java
DisbursementConfiguration disbursementConfiguration = new DisbursementConfiguration("<place your DISBURSEMENT_SUBSCRIPTION_KEY>", "<place your REFERENCE_ID>", "<place your API_KEY>");
```

For Remittance create an instance of `RemittanceConfiguration`.

```java
RemittanceConfiguration remittanceConfiguration = new RemittanceConfiguration("<place your REMITTANCE_SUBSCRIPTION_KEY>", "<place your REFERENCE_ID>", "<place your API_KEY>");
```

You are required to create the corresponding `<SUBSCRIPTION_TYPE>Configuration` instance as mentioned above, before making an API request with the SDK.

Following are the default parameters required in the **SANDBOX** mode

1. `SUBSCRIPTION_KEY` (can be obtained from the [developer portal](https://momodeveloper.mtn.com/products) for each product)
2. `referenceId` or the `API User` (can be obtained through [`Sandbox User Provisioning`](https://momodeveloper.mtn.com/docs/services/sandbox-provisioning-api/operations/post-v1_0-apiuser) or from Merchant Portal for production)
3. `apiKey` the API Key (can be obtained through [`Sandbox User Provisioning`](https://momodeveloper.mtn.com/docs/services/sandbox-provisioning-api/operations/post-v1_0-apiuser) or from Merchant Portal for production)

Optional parameters available for `<SUBSCRIPTION_TYPE>Configuration` class are:

1. `Environment` value can be one of the following
    - `Environment.SANDBOX` for Sandbox
    - `Environment.PRODUCTION` for Production
2. `targetEnvironment` value can be one of the following
    - A String that starts with `mtn` and followed by the `country name` `Eg:- mtnuganda` for Production Environment
    - `sandbox` for Sandbox Environment

Alternately, you can override the default configuration by using the following, explained here with an example using `CollectionConfiguration`:

```java
CollectionConfiguration collectionConfiguration = new CollectionConfiguration("<COLLECTION_SUBSCRIPTION_KEY>", "<REFERENCE_ID>", "<API_KEY>", "<mode>", "<targetEnvironment>");
```
For example:
```java
CollectionConfiguration collectionConfiguration = new CollectionConfiguration("<COLLECTION_SUBSCRIPTION_KEY>", "<REFERENCE_ID>", "<API_KEY>", Environment.SANDBOX, TargetEnvironment.sandbox.getValue());
```

`<SUBSCRIPTION_TYPE>Configuration` class has one optional method `addCallBackUrl()`:

- `addCallBackUrl()` - Can be used in your application where you want MoMo API to push data as a `PUT` request. This option is only available for API calls that accepts `X-Callback-Url` as a header. You can use this if you wish to specify different callback urls for different use cases. You can pass the callback url with each request seperately. 

One important thing to keep in mind is that the `CallBackUrl` is required to have a host address which is specified during the initial phase of user creation. In the sandbox environment, a `CallBackHost` is passed in as a parameter during `API User` creation. The `CallBackUrl` must have the same host address as the `CallBackHost`. In production, it needs to be specified in the Merchant Portal. 

Eg:- If `http://webhook.site/b6491e9b-8068-4703-8e21-ede56749c28a` is the `CallBackUrl`, then `webhook.site` is the `CallBackHost`. 

`CallBackUrl` can be used in the following methods

**Method 1** `addCallBackUrl` method called with parameter `CallBackUrl` during the configuration will act as a global value and can be used for all requests. 
```java
CollectionConfiguration collectionConfiguration = new CollectionConfiguration("<COLLECTION_SUBSCRIPTION_KEY>", "<REFERENCE_ID>", "<API_KEY>", Environment.SANDBOX, TargetEnvironment.sandbox.getValue()).addCallBackUrl("<CallBackUrl>");
```
**Method 2** `addCallBackUrl` method called with parameter `CallBackUrl` on the `collectionRequest` object will override the global value and all subsequent calls with `collectionRequest` object will have this custom `CallBackUrl`.
```java
CollectionConfiguration collectionConfiguration = new CollectionConfiguration("<COLLECTION_SUBSCRIPTION_KEY>", "<REFERENCE_ID>", "<API_KEY>", Environment.SANDBOX, TargetEnvironment.sandbox.getValue()).addCallBackUrl("<CallBackUrl>");
CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

collectionRequest.addCallBackUrl("<CallBackUrl>").requestToPay(<requestPayObject>)
```
**Method 3** `addCallBackUrl` method called with parameter `CallBackUrl` on the `collectionConfiguration.createCollectionRequest()` object will override the global value, but here only this request is using the custom url, since we have not initialised any object of `CollectionRequest` during the request.
```java
CollectionConfiguration collectionConfiguration = new CollectionConfiguration("<COLLECTION_SUBSCRIPTION_KEY>", "<REFERENCE_ID>", "<API_KEY>", Environment.SANDBOX, TargetEnvironment.sandbox.getValue()).addCallBackUrl("<CallBackUrl>");
CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

collectionConfiguration.createCollectionRequest().addCallBackUrl("<CallBackUrl>").requestToPay(<requestPayObject>)
```



## Handling Errors

Error handling is a crucial aspect of software development. Both expected and unexpected errors should be handled by your code.

This Java SDK provides a `MoMoException` class that is used for common scenarios where exceptions are thrown. The `getMessage()` method can provide useful information to understand the cause of errors.

```java
Payer payer = new Payer();
payer.setPartyId("<MSISDN_NUMBER>");
payer.setPartyIdType("<MSISDN>");

RequestPay requestPay = new RequestPay();
requestPay.setAmount("6.0");
requestPay.setCurrency("EUR");
requestPay.setExternalId("6353636");
requestPay.setPayeeNote("payer note");
requestPay.setPayerMessage("Pay for product a");
requestPay.setPayer(payer);

CollectionConfiguration collectionConfiguration = new CollectionConfiguration("<COLLECTION_SUBSCRIPTION_KEY>", "<REFERENCE_ID>", "<API_KEY>", Environment.SANDBOX, TargetEnvironment.sandbox.getValue()).addCallBackUrl(CALLBACK_URL);
CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

try {
    collectionRequest.requestToPay(null);
} catch (MoMoException e) {
    System.out.println(e.getMessage());
}
```

Sample Response:

```java
{
  "errorCategory": "Validation",
  "errorCode": "MandatoryValueNotSupplied",
  "errorDescription": "Request to Pay object is not initialized",
  "errorDateTime": "2023-02-20 19:51:39 IST"
}
```


## Use Cases

-   [Sandbox User Provisioning](#sandbox)
-   [Collection](#collection)
-   [Disbursement](#disbursement)
-   [Remittance](#remittance)

### Sandbox User Provisioning

<table>
<thead>
  <tr>
    <th>Scenarios</th>
    <th>API</th>
    <th>Function</th>
    <th>Parameters</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td rowspan="3">Sandbox User Provisioning</td>
    <td><a href="docs/sandboxUserProvisioning/createUser.Readme.md">Creating an API user</a></td>
    <td>createUser</td>
    <td>CallbackHost callbackHost</td>
  </tr>
  <tr>
    <td><a href="docs/sandboxUserProvisioning/getUserDetails.Readme.md">Get user details with referenceId</a></td>
    <td>getUserDetails</td>
    <td>String referenceId</td>
  </tr>
  <tr>
    <td><a href="docs/sandboxUserProvisioning/createApiKey.Readme.md">Create ApiKey for a user</a></td>
    <td>createApiKey</td>
    <td>String referenceId</td>
  </tr>
</tbody>
</table>

### Collection

<table>
<thead>
  <tr>
    <th>Scenarios</th>
    <th>API</th>
    <th>Function</th>
    <th>Parameters</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td rowspan="3">Request To Pay</td>
    <td><a href="docs/collection/requestToPay.Readme.md">Request To Pay</a></td>
    <td>requestToPay</td>
    <td>RequestPay requestPay</td>
  </tr>
  <tr>
    <td><a href="docs/collection/requestToPayTransactionStatus.Readme.md">Request To Pay Transaction Status</a></td>
    <td>requestToPayTransactionStatus</td>
    <td>String referenceId</td>
  </tr>
  <tr>
    <td><a href="docs/collection/requestToPayDeliveryNotification.Readme.md">Request To Pay Delivery Notification</a></td>
    <td>requestToPayDeliveryNotification</td>
    <td>String referenceId, DeliveryNotification deliveryNotification, (optional)String deliveryNotificationHeader</td>
  </tr>
  <tr>
    <td rowspan="3">Request To Withdraw</td>
    <td><a href="docs/collection/requestToWithdrawV1.Readme.md">Request To Withdraw-V1</a></td>
    <td>requestToWithdrawV1</td>
    <td>Withdraw withdraw</td>
  </tr>
  <tr>
    <td><a href="docs/collection/requestToWithdrawV2.Readme.md">Request To Withdraw-V2</a></td>
    <td>requestToWithdrawV2</td>
    <td>Withdraw withdraw</td>
  </tr>
  <tr>
    <td><a href="docs/collection/requestToWithdrawTransactionStatus.Readme.md">Request To Withdraw Transaction Status</a></td>
    <td>requestToWithdrawTransactionStatus</td>
    <td>String referenceId</td>
  </tr>
  <tr>
    <td>Get UserInfo With Consent</td>
    <td><a href="docs/collection/getUserInfoWithConsent.Readme.md">Get UserInfo With Consent</a></td>
    <td>getUserInfoWithConsent</td>
    <td>AccountHolder accountHolder, String scope, AccessType accessType</td>
  </tr>
  <tr>
    <td>Get Basic Userinfo</td>
    <td><a href="docs/collection/getBasicUserinfo.Readme.md">Get Basic Userinfo</a></td>
    <td>getBasicUserinfo</td>
    <td>String msisdn</td>
  </tr>
  <tr>
    <td>Validate Account Holder Status</td>
    <td><a href="docs/collection/validateAccountHolderStatus.Readme.md">Validate Account Holder Status</a></td>
    <td>validateAccountHolderStatus</td>
    <td>AccountHolder accountHolder</td>
  </tr>
  <tr>
    <td>Get Account Balance</td>
    <td><a href="docs/collection/getAccountBalance.Readme.md">Get Account Balance</a></td>
    <td>getAccountBalance</td>
    <td>NA</td>
  </tr>
  <tr>
    <td>Get Account Balance In Specific Currency</td>
    <td><a href="docs/collection/getAccountBalanceInSpecificCurrency.Readme.md">Get Account Balance In Specific Currency</a></td>
    <td>getAccountBalanceInSpecificCurrency</td>
    <td>String currency</td>
  </tr>
</tbody>
</table>

### Disbursement

<table>
<thead>
  <tr>
    <th>Scenarios</th>
    <th>API</th>
    <th>Function</th>
    <th>Parameters</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td rowspan="2">Transfer</td>
    <td><a href="docs/disbursement/transfer.Readme.md">Transfer</a></td>
    <td>transfer</td>
    <td>Transfer transfer</td>
  </tr>
  <tr>
    <td><a href="docs/disbursement/getTransferStatus.Readme.md">Get Transfer Status</a></td>
    <td>getTransferStatus</td>
    <td>String referenceId</td>
  </tr>
  <tr>
    <td>Request To Pay Delivery Notification</td>
    <td><a href="docs/disbursement/requestToPayDeliveryNotification.Readme.md">Request To Pay Delivery Notification</a></td>
    <td>requestToPayDeliveryNotification</td>
    <td>String referenceId, DeliveryNotification deliveryNotification, (optional)String deliveryNotificationHeader</td>
  </tr>
  <tr>
    <td rowspan="3">Deposit</td>
    <td><a href="docs/disbursement/depositV1.Readme.md">Deposit-V1</a></td>
    <td>depositV1</td>
    <td>Deposit deposit</td>
  </tr>
  <tr>
    <td><a href="docs/disbursement/depositV2.Readme.md">Deposit-V2</a></td>
    <td>depositV2</td>
    <td>Deposit deposit</td>
  </tr>
  <tr>
    <td><a href="docs/disbursement/getDepositStatus.Readme.md">Get Deposit Status</a></td>
    <td>getDepositStatus</td>
    <td>String referenceId</td>
  </tr>
  <tr>
    <td rowspan="3">Refund</td>
    <td><a href="docs/disbursement/refundV1.Readme.md">Refund-V1</a></td>
    <td>refundV1</td>
    <td>Refund refund</td>
  </tr>
  <tr>
    <td><a href="docs/disbursement/refundV2.Readme.md">Refund-V2</a></td>
    <td>refundV2</td>
    <td>Refund refund</td>
  </tr>
  <tr>
    <td><a href="docs/disbursement/getRefundStatus.Readme.md">Get Refund Status</a></td>
    <td>getRefundStatus</td>
    <td>String referenceId</td>
  </tr>
  <tr>
    <td>Get UserInfo With Consent</td>
    <td><a href="docs/disbursement/getUserInfoWithConsent.Readme.md">Get UserInfo With Consent</a></td>
    <td>getUserInfoWithConsent</td>
    <td>AccountHolder accountHolder, String scope, AccessType accessType</td>
  </tr>
  <tr>
    <td>Get Basic Userinfo</td>
    <td><a href="docs/disbursement/getBasicUserinfo.Readme.md">Get Basic Userinfo</a></td>
    <td>getBasicUserinfo</td>
    <td>String msisdn</td>
  </tr>
  <tr>
    <td>Validate Account Holder Status</td>
    <td><a href="docs/disbursement/validateAccountHolderStatus.Readme.md">Validate Account Holder Status</a></td>
    <td>validateAccountHolderStatus</td>
    <td>AccountHolder accountHolder</td>
  </tr>
  <tr>
    <td>Get Account Balance</td>
    <td><a href="docs/disbursement/getAccountBalance.Readme.md">Get Account Balance</a></td>
    <td>getAccountBalance</td>
    <td>NA</td>
  </tr>
  <tr>
    <td>Get Account Balance In Specific Currency</td>
    <td><a href="docs/disbursement/getAccountBalanceInSpecificCurrency.Readme.md">Get Account Balance In Specific Currency</a></td>
    <td>getAccountBalanceInSpecificCurrency</td>
    <td>String currency</td>
  </tr>
</tbody>
</table>

### Remittance

<table>
<thead>
  <tr>
    <th>Scenarios</th>
    <th>API</th>
    <th>Function</th>
    <th>Parameters</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td rowspan="2">Transfer</td>
    <td><a href="docs/remittance/transfer.Readme.md">Transfer</a></td>
    <td>transfer</td>
    <td>Transfer transfer</td>
  </tr>
  <tr>
    <td><a href="docs/remittance/getTransferStatus.Readme.md">Get Transfer Status</a></td>
    <td>getTransferStatus</td>
    <td>String referenceId</td>
  </tr>
  <tr>
    <td>Request To Pay Delivery Notification</td>
    <td><a href="docs/remittance/requestToPayDeliveryNotification.Readme.md">Request To Pay Delivery Notification</a></td>
    <td>requestToPayDeliveryNotification</td>
    <td>String referenceId, DeliveryNotification deliveryNotification, (optional)String deliveryNotificationHeader</td>
  </tr>
  <tr>
    <td>Get UserInfo With Consent</td>
    <td><a href="docs/remittance/getUserInfoWithConsent.Readme.md">Get UserInfo With Consent</a></td>
    <td>getUserInfoWithConsent</td>
    <td>AccountHolder accountHolder, String scope, AccessType accessType</td>
  </tr>
  <tr>
    <td>Get Basic Userinfo</td>
    <td><a href="docs/remittance/getBasicUserinfo.Readme.md">Get Basic Userinfo</a></td>
    <td>getBasicUserinfo</td>
    <td>String msisdn</td>
  </tr>
  <tr>
    <td>Validate Account Holder Status</td>
    <td><a href="docs/remittance/validateAccountHolderStatus.Readme.md">Validate Account Holder Status</a></td>
    <td>validateAccountHolderStatus</td>
    <td>AccountHolder accountHolder</td>
  </tr>
  <tr>
    <td>Get Account Balance</td>
    <td><a href="docs/remittance/getAccountBalance.Readme.md">Get Account Balance</a></td>
    <td>getAccountBalance</td>
    <td>NA</td>
  </tr>
</tbody>
</table>

## Testing

The `test` package contains the test cases. These are logically divided into unit and integration tests. The test commands(Eg:- `mvn test` ) must be run from the directory where `pom.xml` file is located.

### Unit tests

Those tests are located in `src/test/java/com/momo/api/unit` and are responsible for ensuring each class is behaving as expected, 
without considering the rest of the system. Unit tests heavily leverage `mocking` and are an essential part of our testing harness.

To run unit tests,

```java
mvn test -Dtest=com.momo.api.unit.**
```

### Integration tests

Those tests are located in `src/test/java/com/momo/api/integration` and are responsible for ensuring a proper communication with server/simulator. 
With the integration tests, we ensure all communications between the SDK and the server/simulator are behaving accordingly.

For integration test:

-   You will need a valid `subscription key`, `reference id` (also known as `api user` ) and `apiKey`.
-   Copy the config.properties.sample file to config.properties and enter your credentials in the appropriate fields.

To run the integration tests,

```java
mvn test -Dtest=com.momo.api.integration.**
```

To run tests individually:

```java
mvn test -Dtest=path/to/test/class/file
```

For example:

```java
mvn test -Dtest=com.momo.api.unit.CollectionRequestTest.java
```

### Execute all tests (unit + integration)

```java
mvn test
```

## Samples

The sample code snippets are all completely independent and self-contained. You can analyze them to get an understanding of how a particular method can be implemented in your application. Sample code snippets can be found [here](momoapi-java-sdk-samples/src/). Steps to run the sample code snippets are as follows:

- Clone this repository:

```java
git clone https://github.com/gsmainclusivetechlab/momoapi-java-sdk.git
```

- Import `momoapi-java-sdk-samples` project into your IDE
- Import 'momoapi-java-sdk' jar file into your `momoapi-java-sdk-samples` project's classpath
- Rename `config.properties.sample` file in `src\test\resources` to `config.properties` and replace placeholders with values for your `consumer key`, `consumer secret` and `api key`.

For example:

```java
COLLECTION_SUBSCRIPTION_KEY=<your_collection_subscription_key_here>
DISBURSEMENT_SUBSCRIPTION_KEY=<your_disbursement_subscription_key_here>
REMITTANCE_SUBSCRIPTION_KEY=<your_remittance_subscription_key_here>
CONSUMER_SECRET=<your_consumer_secret_here>
REFERENCE_ID=<your_api_user/referenceId_value _here>
API_KEY=<your_api_key_here>
CALLBACK_URL=<your_callback_url_here>
```

You are now ready to run your sample codes.

- Run individual samples
