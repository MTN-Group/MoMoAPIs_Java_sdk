# Validate AccountHolder Status

`Here, validateAccountHolderStatus(AccountHolder accountHolder) sends a GET request to /collection/v1_0/accountholder/{accountHolderIdType}/{accountHolderId}/active`

> `Operation is used to check if an account holder is registered and active in the system.`

### Usage/Examples

```java
CollectionConfiguration collectionConfiguration = new CollectionConfiguration("<COLLECTION_SUBSCRIPTION_KEY>", "<REFERENCE_ID>", "<API_KEY>","<MODE>","<TARGET_ENVIRONMENT>");
CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

AccountHolder accountHolder = new AccountHolder(IdType.msisdn.getValue(), "<MSISDN>");
Result result = collectionRequest.validateAccountHolderStatus(accountHolder);
```

### Response Example

```java
{
  "result": true
}
```