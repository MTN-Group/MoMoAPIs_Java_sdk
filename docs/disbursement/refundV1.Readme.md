# Refund V1

`Here, refundV1(Refund refund) sends a POST request to /disbursement/v1_0/refund`

> `This operation is used to refund an amount from the owner’s account to a payee account.
Status of the transaction can be validated by using the GET /refund/{referenceId}`

### Usage/Examples

```java
DisbursementConfiguration disbursementConfiguration = new DisbursementConfiguration("<DISBURSEMENT_SUBSCRIPTION_KEY>", "<REFERENCE_ID>", "<API_KEY>","<MODE>","<TARGET_ENVIRONMENT>").addCallBackUrl("<CALLBACK_URL>");
DisbursementRequest disbursementRequest = disbursementConfiguration.createDisbursementRequest();

Refund refund = new Refund();
refund.setAmount("6.0");
refund.setCurrency("EUR");
refund.setExternalId("6353636");
refund.setPayeeNote("payer note");
refund.setPayerMessage("Pay for product a");
refund.setReferenceIdToRefund("<REQUEST_TO_PAY_REFERENCE_ID>");

StatusResponse statusResponse = disbursementRequest.refundV1(refund);

```

### Response Example

```java
{
  "status": true,
  "referenceId": "016d5f73-4000-4fc6-a4b8-6d16af5b45f1"
}
```