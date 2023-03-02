# Get Refund Status

`Here, getRefundStatus(String referenceId) sends a GET request to /disbursement/v1_0/refund/{referenceId}`

> `This operation is used to get the status of a refund (of versions V1 and V2). X-Reference-Id that was passed in the POST is used as reference to this request.`

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
    requestPay.setPayerMessage("Pay for product a");
    requestPay.setPayer(payer);
            
    StatusResponse statusResponse = collectionRequest.requestToPay(requestPay);


DisbursementConfiguration disbursementConfiguration = new DisbursementConfiguration("<DISBURSEMENT_SUBSCRIPTION_KEY>", "<REFERENCE_ID>", "<API_KEY>","<MODE>","<TARGET_ENVIRONMENT>").addCallBackUrl("<CALLBACK_URL>");
DisbursementRequest disbursementRequest = disbursementConfiguration.createDisbursementRequest();

Refund refund = new Refund();
 refund.setAmount("6.0");
 refund.setCurrency("EUR");
 refund.setExternalId("6353636");
 refund.setPayeeNote("payer note");
 refund.setPayerMessage("Pay for product a");
 refund.setReferenceIdToRefund(statusResponse.getReferenceId());

statusResponse = disbursementRequest.refundV1(refund);

RefundStatus refundStatus = disbursementRequest.getRefundStatus(statusResponse.getReferenceId());
```

### Response Example

```java
{
    "payee": {
    "partyIdType": "MSISDN",
    "partyId": "23423423450"
  },
  "financialTransactionId": "1947913865",
  "status": "SUCCESSFUL",
  "amount": "6",
  "currency": "EUR",
  "externalId": "6353636",
  "payerMessage": "Pay for product a",
  "payeeNote": "payer note"
}
```