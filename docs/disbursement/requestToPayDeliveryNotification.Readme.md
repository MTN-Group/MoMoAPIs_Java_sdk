# Request To Pay Delivery Notification

`Here, requestToPayDeliveryNotification(String referenceId, DeliveryNotification deliveryNotification) sends a POST request to /disbursement/v1_0/requesttopay/{referenceId}/deliverynotification`

> `This operation is used to send additional Notification to an End User.`

### Usage/Examples

```java
DisbursementConfiguration disbursementConfiguration = new DisbursementConfiguration("<DISBURSEMENT_SUBSCRIPTION_KEY>", "<REFERENCE_ID>", "<API_KEY>","<MODE>","<TARGET_ENVIRONMENT>").addCallBackUrl("<CALLBACK_URL>");
DisbursementRequest disbursementRequest = disbursementConfiguration.createDisbursementRequest();

DeliveryNotification deliveryNotification = new DeliveryNotification();
deliveryNotification.setNotificationMessage("test message");

StatusResponse statusResponse = disbursementRequest.requestToPayDeliveryNotification("<REQUEST_TO_PAY_REFERENCE_ID>",            
                                deliveryNotification);
```

### Response Example

```java
{
  "status": true
}
```