# Get User Details

`Here, getUserDetails(String referenceId) sends a GET request to /v1_0/apiuser/{X-Reference-Id}`

> `Used to get the information of API user that we created using createUser()`

### Usage/Examples

```java
CallbackHost callbackHost = new CallbackHost("<CALLBACK_HOST_URL>");
UserProvisioningConfiguration userProvisioningConfiguration = new UserProvisioningConfiguration("<SUBSCRIPTION_KEY>");
UserProvisioningRequest userProvisioningRequest = userProvisioningConfiguration.createUserProvisioningRequest();

StatusResponse statusResponse = userProvisioningRequest.createUser(callbackHost);

ApiUser apiUser = userProvisioningRequest.getUserDetails(statusResponse.getReferenceId());
```

### Response Example

```java
{
  "targetEnvironment": "sandbox",
  "providerCallbackHost": "sample.callback.host"
}
```