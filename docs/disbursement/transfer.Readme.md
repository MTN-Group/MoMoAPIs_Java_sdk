# Transfer

`Here, transfer(Transfer transfer) sends a POST request to /disbursement/v1_0/transfer`

> `Transfer operation is used to transfer an amount from the own account to a payee account.
Status of the transaction can be validated by using the GET /transfer/{referenceId}`

### Usage/Examples

```java
DisbursementConfiguration disbursementConfiguration = new DisbursementConfiguration("<DISBURSEMENT_SUBSCRIPTION_KEY>", "<REFERENCE_ID>", "<API_KEY>","<MODE>","<TARGET_ENVIRONMENT>").addCallBackUrl("<CALLBACK_URL>");
DisbursementRequest disbursementRequest = disbursementConfiguration.createDisbursementRequest();

Payee payee = new Payee();
payee.setPartyId("<MSISDN>");
payee.setPartyIdType(IdType.MSISDN.getValue());

Transfer transfer = new Transfer();
transfer.setAmount("6.0");
transfer.setCurrency("EUR");
transfer.setExternalId("6353636");
transfer.setPayeeNote("payer note");
transfer.setPayerMessage("Pay for product a");
transfer.setPayee(payee);

StatusResponse statusResponse = disbursementRequest.transfer(transfer);
```

### Response Example

```java
{
  "status": true,
  "referenceId": "2d3ae1f4-abe9-477e-a962-e276a5863038"
}
```