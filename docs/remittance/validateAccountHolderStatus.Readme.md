# Validate AccountHolder Status

`Here, validateAccountHolderStatus(AccountHolder accountHolder) sends a GET request to /remittance/v1_0/accountholder/{accountHolderIdType}/{accountHolderId}/active`

> `Operation is used to check if an account holder is registered and active in the system.`

### Usage/Examples

```java
RemittanceConfiguration remittanceConfiguration = new RemittanceConfiguration("<Remittance_SUBSCRIPTION_KEY>", "<REFERENCE_ID>", "<API_KEY>","<MODE>","<TARGET_ENVIRONMENT>");
RemittanceRequest remittanceRequest = remittanceConfiguration.createRemittanceRequest();

AccountHolder accountHolder = new AccountHolder(IdType.msisdn.getValue(), "<MSISDN>");
Result result = remittanceRequest.validateAccountHolderStatus(accountHolder);
```

### Response Example

```java
{
  "result": true
}
```