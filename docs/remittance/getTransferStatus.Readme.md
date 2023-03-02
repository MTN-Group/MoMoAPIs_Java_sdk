# Get Transfer Status

`Here, getTransferStatus(String referenceId) sends a GET request to /remittance/v1_0/transfer/{referenceId}`

> `This operation is used to get the status of a transfer. X-Reference-Id that was passed in the POST is used as reference to the request`

### Usage/Examples

```java
RemittanceConfiguration remittanceConfiguration = new RemittanceConfiguration("<REMITTANCE_SUBSCRIPTION_KEY>", "<REFERENCE_ID>", "<API_KEY>","<MODE>","<TARGET_ENVIRONMENT>").addCallBackUrl("<CALLBACK_URL>");
RemittanceRequest remittanceRequest = remittanceConfiguration.createRemittanceRequest();

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

StatusResponse statusResponse = remittanceRequest.transfer(transfer);

TransferStatus transferStatus = remittanceRequest.getTransferStatus(statusResponse.getReferenceId());
```

### Response Example

```java
{
  "payee": {
    "partyIdType": "MSISDN",
    "partyId": "23423423450"
  },
  "financialTransactionId": "1323837755",
  "status": "SUCCESSFUL",
  "amount": "6",
  "currency": "EUR",
  "externalId": "6353636",
  "payerMessage": "Pay for product a",
  "payeeNote": "payer note"
}
```