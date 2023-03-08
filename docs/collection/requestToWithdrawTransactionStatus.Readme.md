# Request To Withdraw Transaction Status

`Here, requestToWithdrawTransactionStatus(String referenceId) sends a GET request to /collection/v1_0/requesttowithdraw/{referenceId}`

> `This operation is used to get the status of a request to withdraw (of both versions V1 and V2). X-Reference-Id that was passed in the POST is used as reference to the request.`

### Usage/Examples

```java
CollectionConfiguration collectionConfiguration = new CollectionConfiguration("<COLLECTION_SUBSCRIPTION_KEY>", "<REFERENCE_ID>", "<API_KEY>","<MODE>","<TARGET_ENVIRONMENT>").addCallBackUrl("<CALLBACK_URL>");
CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

Payer payer = new Payer();
payer.setPartyId("<MSISDN>");
payer.setPartyIdType(IdType.MSISDN.getValue());

Withdraw withdraw = new Withdraw();
withdraw.setAmount("6.0");
withdraw.setCurrency("EUR");
withdraw.setExternalId("6353636");
withdraw.setPayeeNote("payer note");
withdraw.setPayerMessage("Pay for product a");
withdraw.setPayer(payer);

StatusResponse statusResponse = collectionRequest.requestToWithdrawV1(withdraw);

WithdrawStatus withdrawStatus = collectionRequest.requestToWithdrawTransactionStatus(statusResponse.getReferenceId());
```

### Response Example

```java
{
  "payer": {
    "partyIdType": "MSISDN",
    "partyId": "23423423450"
  },
  "financialTransactionId": "85974752",
  "status": "SUCCESSFUL",
  "amount": "6",
  "currency": "EUR",
  "externalId": "6353636",
  "payerMessage": "Pay for product a",
  "payeeNote": "payer note"
}
```