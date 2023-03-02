# Get AccountBalance In Specific Currency

`Here, getAccountBalanceInSpecificCurrency(String currency) sends a GET request to /collection/v1_0/account/balance/{currency}`

> `Get the balance of the account. Currency parameter passed in GET request`

### Usage/Examples

```java
CollectionConfiguration collectionConfiguration = new CollectionConfiguration("<COLLECTION_SUBSCRIPTION_KEY>", "<REFERENCE_ID>", "<API_KEY>", "<MODE>", "<TARGET_ENVIRONMENT>");
CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

AccountBalance accountBalance = collectionRequest.getAccountBalanceInSpecificCurrency("<CURRENCY>");
```

### Response Example

```java
{
  "availableBalance": "0",
  "currency": "EUR"
}
```