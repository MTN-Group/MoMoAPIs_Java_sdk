# Get AccountBalance

`Here, getAccountBalance() sends a GET request to /collection/v1_0/account/balance`

> `Get the balance of the account.`

### Usage/Examples

```java
CollectionConfiguration collectionConfiguration = new CollectionConfiguration("<COLLECTION_SUBSCRIPTION_KEY>", "<REFERENCE_ID>", "<API_KEY>", "<MODE>","<TARGET_ENVIRONMENT>");
CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

AccountBalance accountBalance = collectionRequest.getAccountBalance();
```

### Response Example

```java
{
  "availableBalance": "0",
  "currency": "EUR"
}
```