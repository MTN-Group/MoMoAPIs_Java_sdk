# Deposit V1

`Here, depositV1(Deposit deposit) sends a POST request to /disbursement/v1_0/deposit`

> `This operation is used to deposit an amount from the owner’s account to a payee account.
Status of the transaction can be validated by using the GET /deposit/{referenceId}`

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
```

### Response Example

```java
{
  "status": true,
  "referenceId": "f104e738-1f09-4e3c-a1ea-be0d4b097ed6"
}
```