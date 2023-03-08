# MTN MoMo API - Sample SDK for Java

These sample code snippets are all completely independent and self-contained. You can analyze them to get an understanding of how a particular method can be implemented in your application. 
In order to run these sample codes, you must have a valid `subscription key`, `reference id` (also known as `api user` ) and `apiKey`.


## Requirements

-   Java JDK-1.8 or higher
-   Apache Maven 3 or higher


## Setup

- Clone https://github.com/gsmainclusivetechlab/momoapi-java-sdk.git
- Import `momoapi-java-sdk-samples` project into your IDE
- Copy 'momoapi-java-sdk' jar file from `jar` folder to your project's classpath or import the jar file using your IDE. Alternately you can build `momoapi-java-sdk` project with tests using 'mvn clean package' command or without tests using 'mvn clean package -Dmaven.test.skip=true' command and then copy or import this jar file to `momoapi-java-sdk-samples`
- Rename `config.properties.sample` file in `src\test\resources` to `config.properties` and replace placeholders with values for your `subscription key`, `reference id` (also known as `api user` ) and `apiKey`.

For example:

```java
COLLECTION_SUBSCRIPTION_KEY=<your_collection_subscription_key_here>
DISBURSEMENT_SUBSCRIPTION_KEY=<your_disbursement_subscription_key_here>
REMITTANCE_SUBSCRIPTION_KEY=<your_remittance_subscription_key_here>
CONSUMER_SECRET=<your_consumer_secret_here>
REFERENCE_ID=<your_api_user/referenceId_value _here>
API_KEY=<your_api_key_here>
CALLBACK_URL=<your_callback_url_here>
```

You are now ready to run your sample codes through your IDE.

- Run individual samples