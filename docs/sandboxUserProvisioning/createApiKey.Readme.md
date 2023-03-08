# Create ApiKey

`Here, createApiKey(String referenceId) sends a POST request to /v1_0/apiuser/{X-Reference-Id}/apikey`

> `Used to create an API key for an API user in the sandbox environment.`

### Usage/Examples

```java
CallbackHost callbackHost = new CallbackHost("<CALLBACK_HOST_URL>");
UserProvisioningConfiguration userProvisioningConfiguration = new UserProvisioningConfiguration("<SUBSCRIPTION_KEY>");
UserProvisioningRequest userProvisioningRequest = userProvisioningConfiguration.createUserProvisioningRequest();

StatusResponse statusResponse = userProvisioningRequest.createUser(callbackHost);

ApiKey apiKey = userProvisioningRequest.createApiKey(statusResponse.getReferenceId());
```

### Response Example

```java
{
  "apiKey": "50bfd1560a7e4ff981f4ba0c2a07b40d"
}
```