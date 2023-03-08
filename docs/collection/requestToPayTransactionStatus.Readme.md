# Request To Pay Transaction Status

`Here, requestToPayTransactionStatus(String referenceId) sends a GET request to /collection/v1_0/requesttopay/{referenceId}`

> `This operation is used to get the status of a request to pay. X-Reference-Id that was passed in the POST is used as reference to this request.`

### Usage/Examples

```java
CollectionConfiguration collectionConfiguration = new CollectionConfiguration("<COLLECTION_SUBSCRIPTION_KEY>", "<REFERENCE_ID>", "<API_KEY>","<MODE>","<TARGET_ENVIRONMENT>").addCallBackUrl("<CALLBACK_URL>");
CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

Payer payer = new Payer();
payer.setPartyId("<MSISDN>");
payer.setPartyIdType(IdType.MSISDN.getValue());

RequestPay requestPay = new RequestPay();
requestPay.setAmount("6.0");
requestPay.setCurrency("EUR");
requestPay.setExternalId("6353636");
requestPay.setPayeeNote("payer note");
requestPay.setPayerMessage("Pay for product test");
requestPay.setPayer(payer);

StatusResponse statusResponse = collectionRequest.requestToPay(requestPay);

RequestPayStatus requestPayStatus = collectionRequest.requestToPayTransactionStatus(statusResponse.getReferenceId());
```

### Response Example

```java
{
  "payer": {
    "partyIdType": "MSISDN",
    "partyId": "23423423450"
  },
  "financialTransactionId": "1806336820",
  "status": "SUCCESSFUL",
  "amount": "6",
  "currency": "EUR",
  "externalId": "6353636",
  "payerMessage": "Pay for product test",
  "payeeNote": "payer note"
}
```