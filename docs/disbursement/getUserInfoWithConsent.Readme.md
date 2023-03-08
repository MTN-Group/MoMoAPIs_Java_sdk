# Get User Information with Consent using Oauth2

`Here, getUserInfoWithConsent(AccountHolder accountHolder, String scope, AccessType accesType) sends a GET request to /disbursement/oauth2/v1_0/userinfo. Prior to that a POST call will happen to /disbursement/v1_0/bc-authorize and then to /disbursement/oauth2/token/ for fetching the Oauth2 token in the background.`

> `This operation returns personal information of the user. The operation needs consent by the user.`

### Usage/Examples

```java
DisbursementConfiguration disbursementConfiguration = new DisbursementConfiguration("<Disbursement_SUBSCRIPTION_KEY>", "<REFERENCE_ID>", "<API_KEY>", "<MODE>","<TARGET_ENVIRONMENT>").addCallBackUrl("<CALLBACK_URL>");
DisbursementRequest disbursementRequest = disbursementConfiguration.createDisbursementRequest();

AccountHolder accountHolder = new AccountHolder("<accountHolderIdType>", "<accountHolderId>");
UserInfo userInfo = disbursementRequest.getUserInfoWithConsent(accountHolder, "<SCOPE>", "<ACCESS_TYPE>");
```

### Response Example

```java
{
    "sub": "0",
    "name": "Sand Box",
    "given_name": "Sand",
    "family_name": "Box",
    "birthdate": "1976-08-13",
    "locale": "sv_SE",
    "gender": "MALE",
    "updated_at": 1676538339
}
```
