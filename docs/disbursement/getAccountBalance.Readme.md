# Get Account Balance

`Here, getAccountBalance() sends a GET request to /disbursement/v1_0/account/balance`

> `This operation is used to get the balance of the account.`

### Usage/Examples

```java
DisbursementConfiguration disbursementConfiguration = new DisbursementConfiguration("<DISBURSEMENT_SUBSCRIPTION_KEY>", "<REFERENCE_ID>", "<API_KEY>","<MODE>","<TARGET_ENVIRONMENT>");
DisbursementRequest disbursementRequest = disbursementConfiguration.createDisbursementRequest();

AccountBalance accountBalance = disbursementRequest.getAccountBalance();
```

### Response Example

```java
{
  "availableBalance": "200",
  "currency": "EUR"
}
```