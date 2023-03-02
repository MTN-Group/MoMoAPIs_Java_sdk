# Request To Pay Delivery Notification

`Here, requestToPayDeliveryNotification(String referenceId, DeliveryNotification deliveryNotification) sends a POST request to /collection/v1_0/requesttopay/{referenceId}/deliverynotification`

> `This operation is used to send additional Notification to an End User.`

### Usage/Examples

```java
CollectionConfiguration collectionConfiguration = new CollectionConfiguration("<COLLECTION_SUBSCRIPTION_KEY>", "<REFERENCE_ID>", "<API_KEY>","<MODE>","<TARGET_ENVIRONMENT>").addCallBackUrl("<CALLBACK_URL>");
CollectionRequest collectionRequest = collectionConfiguration.createCollectionRequest();

Payer payer = new Payer();
payer.setPartyId("<MSISDN>");
payer.setPartyIdType(Constants.MSISDN);

RequestPay requestPay = new RequestPay();
requestPay.setAmount("6.0");
requestPay.setCurrency("EUR");
requestPay.setExternalId("6353636");
requestPay.setPayeeNote("payer note");
requestPay.setPayerMessage("Pay for product a");
requestPay.setPayer(payer);

StatusResponse statusResponse = collectionRequest.requestToPay(requestPay);

DeliveryNotification deliveryNotification = new DeliveryNotification();
deliveryNotification.setNotificationMessage("sample message");

statusResponse = collectionRequest.requestToPayDeliveryNotification(statusResponse.getReferenceId(), deliveryNotification);
```

### Response Example

```java
{
  "status": true
}
```

### NOTE

There is another method for requestToPayDeliveryNotification which accepts extra parameters "deliveryNotificationHeader" and "language" where we can attach the message and language in the header if required. It is achieved using following method: requestToPayDeliveryNotification(String referenceId, DeliveryNotification deliveryNotification, String deliveryNotificationHeader, String language)