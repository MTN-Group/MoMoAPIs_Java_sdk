# Create A User

`Here, createUser(CallbackHost callbackHost) sends a POST request to /v1_0/apiuser`

> `Used to create an API user in the sandbox environment.`

### Usage/Examples

```java
CallbackHost callbackHost = new CallbackHost("<CALLBACK_HOST_URL>");
UserProvisioningConfiguration userProvisioningConfiguration = new UserProvisioningConfiguration("<SUBSCRIPTION_KEY>");
UserProvisioningRequest userProvisioningRequest = userProvisioningConfiguration.createUserProvisioningRequest();

StatusResponse statusResponse = userProvisioningRequest.createUser(callbackHost);
```

### Response Example

```java
{
  "referenceId": "03a059de-3867-47a6-8481-fa11effee7a4",
  "status": "true"
}
```