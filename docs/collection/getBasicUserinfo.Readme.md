# Get Basic User Information

`Here, getBasicUserinfo(String msisdn) sends a GET request to /collection/v1_0/accountholder/msisdn/{accountHolderMSISDN}/basicuserinfo`

> `This operation returns personal information of the account holder. The operation does not need any consent by the account holder.`

### Usage/Examples

```java
CollectionConfiguration collectionConfiguration = new CollectionConfiguration("<COLLECTION_SUBSCRIPTION_KEY>", "<REFERENCE_ID>", "<API_KEY>", "<MODE>","<TARGET_ENVIRONMENT>");
CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

BasicUserInfo basicUserInfo = collectionRequest.getBasicUserinfo("<MSISDN>");
```

### Response Example

```java
{
  "given_name": "Sand",
  "family_name": "Box",
  "birthdate": "1976-08-13",
  "locale": "sv_SE",
  "gender": "MALE"
}
```