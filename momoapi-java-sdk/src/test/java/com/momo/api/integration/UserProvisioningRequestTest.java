package com.momo.api.integration;

import com.momo.api.base.HttpStatusCode;
import com.momo.api.base.constants.Constants;
import com.momo.api.base.context.provisioning.UserProvisioningConfiguration;
import com.momo.api.requests.provisioning.UserProvisioningRequest;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.model.StatusResponse;
import com.momo.api.base.util.JSONFormatter;
import com.momo.api.config.PropertiesLoader;
import com.momo.api.models.ApiKey;
import com.momo.api.models.ApiUser;
import com.momo.api.models.CallbackHost;
import java.util.Arrays;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

//TODO UserProvisioningRequestTest Unit test is not written
/**
 *
 * Class UserProvisioningRequestTest
 */
public class UserProvisioningRequestTest {

    static PropertiesLoader loader;

    @BeforeAll
    public static void init() {
        loader = new PropertiesLoader();
    }

    private final String incorrect_referenceId = "incorrect_referenceId";

    @Test
    @DisplayName("Create User Test Success")
    void createUserTestSuccess() throws MoMoException {
        CallbackHost callbackHost = new CallbackHost("webhook.site");
        UserProvisioningConfiguration userProvisioningConfiguration = new UserProvisioningConfiguration(loader.get("COLLECTION_SUBSCRIPTION_KEY"));
        UserProvisioningRequest userProvisioningRequest = userProvisioningConfiguration.createUserProvisioningRequest();

        StatusResponse statusResponse = userProvisioningRequest.createUser(callbackHost);
        assertNotNull(statusResponse);
        assertNotNull(statusResponse.getReferenceId());
        //TODO use this validation in other cases if required
        assertEquals(UUID.fromString(statusResponse.getReferenceId()).toString(), statusResponse.getReferenceId());
    }

    @Test
    @DisplayName("Create User Test Failure")
    void createUserTestFailure() throws MoMoException {
        UserProvisioningConfiguration userProvisioningConfiguration = new UserProvisioningConfiguration(loader.get("COLLECTION_SUBSCRIPTION_KEY"));
        UserProvisioningRequest userProvisioningRequest = userProvisioningConfiguration.createUserProvisioningRequest();

        //case 1: CallbackHost is null
        MoMoException moMoException = assertThrows(MoMoException.class, () -> userProvisioningRequest.createUser(null));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.CALL_BACK_HOST_OBJECT_INIT_ERROR);

        //case 2: CallbackHost object initialised, but varialbes are not defined
        CallbackHost callbackHost = new CallbackHost("");
        moMoException = assertThrows(MoMoException.class, () -> userProvisioningRequest.createUser(callbackHost));
        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.BAD_REQUEST.getHttpStatusCode()));

        //case 2: ProviderCallbackHost field is null
        callbackHost.setProviderCallbackHost(null);
        moMoException = assertThrows(MoMoException.class, () -> userProvisioningRequest.createUser(callbackHost));
        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.BAD_REQUEST.getHttpStatusCode()));
        //TODO test case for invalid ProviderCallbackHost and valid CallbackURL and vice versa
    }

    @Test
    @DisplayName("Get User Details Test Success")
    void getUserDetailsTestSuccess() throws MoMoException {
        CallbackHost callbackHost = new CallbackHost("webhook.site");
        UserProvisioningConfiguration userProvisioningConfiguration = new UserProvisioningConfiguration(loader.get("COLLECTION_SUBSCRIPTION_KEY"));
        UserProvisioningRequest userProvisioningRequest = userProvisioningConfiguration.createUserProvisioningRequest();

        StatusResponse statusResponse = userProvisioningRequest.createUser(callbackHost);

        ApiUser apiUser = userProvisioningRequest.getUserDetails(statusResponse.getReferenceId());
        assertNotNull(apiUser);
        assertEquals(apiUser.getProviderCallbackHost(), "webhook.site");
        assertNotNull(apiUser.getTargetEnvironment());
        //TODO is it sure to be sandbox/production
        assertTrue(Arrays.asList("sandbox", "production").contains(apiUser.getTargetEnvironment()));
    }

    @Test
    @DisplayName("Get User Details Test Failure")
    void getUserDetailsTestFailure() throws MoMoException {
        UserProvisioningConfiguration userProvisioningConfiguration = new UserProvisioningConfiguration(loader.get("COLLECTION_SUBSCRIPTION_KEY"));
        UserProvisioningRequest userProvisioningRequest = userProvisioningConfiguration.createUserProvisioningRequest();

        //case 1: referenceId is null
        MoMoException moMoException = assertThrows(MoMoException.class, () -> userProvisioningRequest.getUserDetails(null));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.NULL_VALUE_ERROR);

        //case 2: referenceId is empty
        moMoException = assertThrows(MoMoException.class, () -> userProvisioningRequest.getUserDetails(""));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.EMPTY_STRING_ERROR);

        //case 3: incorrect referenceId provided
        moMoException = assertThrows(MoMoException.class, () -> userProvisioningRequest.getUserDetails(incorrect_referenceId));
        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.BAD_REQUEST.getHttpStatusCode()));
    }

    @Test
    @DisplayName("Create Api Key Test Success")
    void createApiKeyTestSuccess() throws MoMoException {
        CallbackHost callbackHost = new CallbackHost("webhook.site");
        UserProvisioningConfiguration userProvisioningConfiguration = new UserProvisioningConfiguration(loader.get("COLLECTION_SUBSCRIPTION_KEY"));
        UserProvisioningRequest userProvisioningRequest = userProvisioningConfiguration.createUserProvisioningRequest();

        StatusResponse statusResponse = userProvisioningRequest.createUser(callbackHost);

        ApiKey apiKey = userProvisioningRequest.createApiKey(statusResponse.getReferenceId());
        System.out.println(JSONFormatter.toJSON(apiKey));
        assertNotNull(apiKey);
        assertNotEquals(apiKey, "");
    }

    @Test
    @DisplayName("Create Api Key Test Failure")
    void createApiKeyTestFailure() throws MoMoException {
        UserProvisioningConfiguration userProvisioningConfiguration = new UserProvisioningConfiguration(loader.get("COLLECTION_SUBSCRIPTION_KEY"));
        UserProvisioningRequest userProvisioningRequest = userProvisioningConfiguration.createUserProvisioningRequest();

        //case 1: referenceId is null
        MoMoException moMoException = assertThrows(MoMoException.class, () -> userProvisioningRequest.createApiKey(null));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.NULL_VALUE_ERROR);

        //case 2: referenceId is empty
        moMoException = assertThrows(MoMoException.class, () -> userProvisioningRequest.createApiKey(""));
        assertEquals(moMoException.getError().getErrorDescription(), Constants.EMPTY_STRING_ERROR);

        //case 3: incorrect referenceId provided
        moMoException = assertThrows(MoMoException.class, () -> userProvisioningRequest.createApiKey(incorrect_referenceId));
        assertEquals(moMoException.getError().getStatusCode(), Integer.toString(HttpStatusCode.BAD_REQUEST.getHttpStatusCode()));
    }
}
