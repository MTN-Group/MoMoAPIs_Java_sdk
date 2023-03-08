# Get Deposit Status

`Here, getDepositStatus(String referenceId) sends a GET request to /disbursement/v1_0/deposit/{referenceId}`

> `This operation is used to get the status of a deposit (of versions V1 and V2). X-Reference-Id that was passed in the POST is used as reference to this request.`

### Usage/Examples

```java
DisbursementConfiguration disbursementConfiguration = new DisbursementConfiguration("<DISBURSEMENT_SUBSCRIPTION_KEY>", "<REFERENCE_ID>", "<API_KEY>","<MODE>","<TARGET_ENVIRONMENT>").addCallBackUrl("<CALLBACK_URL>");
DisbursementRequest disbursementRequest = disbursementConfiguration.createDisbursementRequest();

Payee payee = new Payee();
payee.setPartyId("<MSISDN>");
payee.setPartyIdType(IdType.MSISDN.getValue());

Deposit deposit = new Deposit();
deposit.setAmount("6.0");
deposit.setCurrency("EUR");
deposit.setExternalId("6353636");
deposit.setPayeeNote("payer note deposit");
deposit.setPayerMessage("Pay for product a  deposit");
deposit.setPayee(payee);

StatusResponse statusResponse = disbursementRequest.depositV1(deposit);

DepositStatus depositStatus = disbursementRequest.getDepositStatus(statusResponse.getReferenceId());
```

### Response Example

```java
{
  "payee": {
    "partyIdType": "MSISDN",
    "partyId": "23423423450"
  },
  "status": "SUCCESSFUL",
  "amount": "6",
  "currency": "EUR",
  "externalId": "6353636",
  "payerMessage": "Pay for product a  deposit",
  "payeeNote": "payer note deposit"
}
```