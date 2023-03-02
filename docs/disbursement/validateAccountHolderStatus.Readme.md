# Validate AccountHolder Status

`Here, validateAccountHolderStatus(AccountHolder accountHolder) sends a GET request to /disbursement/v1_0/accountholder/{accountHolderIdType}/{accountHolderId}/active`

> `Operation is used to check if an account holder is registered and active in the system.`

### Usage/Examples

```java
DisbursementConfiguration disbursementConfiguration = new DisbursementConfiguration("<DISBURSEMENT_SUBSCRIPTION_KEY>", "<REFERENCE_ID>", "<API_KEY>","<MODE>","<TARGET_ENVIRONMENT>");
DisbursementRequest disbursementRequest = disbursementConfiguration.createDisbursementRequest();

AccountHolder accountHolder = new AccountHolder(IdType.msisdn.getValue(), "<MSISDN>");
Result result = disbursementRequest.validateAccountHolderStatus(accountHolder);
```

### Response Example

```java
{
  "result": true
}
```