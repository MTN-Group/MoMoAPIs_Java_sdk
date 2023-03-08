# Get Basic User info

`Here, getBasicUserinfo(String msisdn) sends a GET request to /remittance/v1_0/accountholder/msisdn/{accountHolderMSISDN}/basicuserinfo`

> `This operation returns personal information of the account holder. The operation does not need any consent by the account holder.`

### Usage/Examples

```java
RemittanceConfiguration remittanceConfiguration = new RemittanceConfiguration("<REMITTANCE_SUBSCRIPTION_KEY>", "<REFERENCE_ID>", "<API_KEY>","<MODE>","<TARGET_ENVIRONMENT>");
RemittanceRequest remittanceRequest = remittanceConfiguration.createRemittanceRequest();

BasicUserInfo basicUserInfo = remittanceRequest.getBasicUserinfo("<MSISDN>");
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
