# Get Account Balance

`Here, getAccountBalance() sends a GET request to /remittance/v1_0/account/balance`

> `This operation is used to get the balance of the account.`

### Usage/Examples

```java
RemittanceConfiguration remittanceConfiguration = new RemittanceConfiguration("<REMITTANCE_SUBSCRIPTION_KEY>", "<REFERENCE_ID>", "<API_KEY>","<MODE>","<TARGET_ENVIRONMENT>");
RemittanceRequest remittanceRequest = remittanceConfiguration.createRemittanceRequest();

AccountBalance accountBalance = remittanceRequest.getAccountBalance();
```

### Response Example

```java
{
  "availableBalance": "200",
  "currency": "EUR"
}
```