# Get Account Balance In Specific Currency

`Here, getAccountBalanceInSpecificCurrency(String currency) sends a GET request to /disbursement/v1_0/account/balance/{currency}`

> `This operation is used to get the balance of the account. Currency parameter is passed in GET`

### Usage/Examples

```java
DisbursementConfiguration disbursementConfiguration = new DisbursementConfiguration("<DISBURSEMENT_SUBSCRIPTION_KEY>", "<REFERENCE_ID>", "<API_KEY>","<MODE>","<TARGET_ENVIRONMENT>");
DisbursementRequest disbursementRequest = disbursementConfiguration.createDisbursementRequest();

AccountBalance accountBalance = disbursementRequest.getAccountBalanceInSpecificCurrency("<CURRENCY>");
```

### Response Example

```java
{
  "availableBalance": "200",
  "currency": "EUR"
}
```