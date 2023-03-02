# Request To Pay

`Here, requestToPay(RequestPay requestPay) sends a POST request to /collection/v1_0/requesttopay`

> `This operation is used to request a payment from a consumer (Payer). The payer will be asked to authorize the payment. The transaction will be executed once the payer has authorized the payment. The requesttopay will be in status PENDING until the transaction is authorized or declined by the payer or it is timed out by the system. Status of the transaction can be validated by using the GET /requesttopay/<resourceId>.`

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
```

### Response Example

```java
{
  "status": true,
  "referenceId": "f104e738-1f09-4e3c-a1ea-be0d4b097ed6"
}
```