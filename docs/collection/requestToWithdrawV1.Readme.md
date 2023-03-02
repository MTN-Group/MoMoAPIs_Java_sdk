# Request To Withdraw-V1

`Here, requestToWithdrawV1(Withdraw withdraw) sends a POST request to /collection/v1_0/requesttowithdraw`

> `This operation is used to request a withdrawal (cash-out) from a consumer (Payer). The payer will be asked to authorize the withdrawal. The transaction will be executed once the payer has authorized the withdrawal`

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
```

### Response Example

```java
{
  "status": true,
  "referenceId": "0e9edd3a-e6f2-4058-81e5-8c2055f0c249"
}
```