package com.momo.api.unit;

import com.momo.api.requests.provisioning.UserProvisioningRequest;
import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.model.StatusResponse;
import com.momo.api.models.ApiKey;
import com.momo.api.models.ApiUser;
import com.momo.api.models.CallbackHost;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

/**
 *
 * Class UserProvisioningRequestTest
 */
public class UserProvisioningRequestTest {
    
    private static final String REFERENCE_ID_RETURNED = UUID.randomUUID().toString();
    private static final String REFERENCE_ID_PARAMETER = UUID.randomUUID().toString();
    
    @Test
    @DisplayName("Create User Test Success")
    void createUserTestSuccess() throws MoMoException {
        UserProvisioningRequest userProvisioningRequestSpy = spy(new UserProvisioningRequest());
        
        CallbackHost callbackHost = new CallbackHost("webhook.site");
        StatusResponse expectedStatusResponse = getExpectedStatusResponse(true);
        doReturn(expectedStatusResponse).when(userProvisioningRequestSpy).createUser(callbackHost);
        
        StatusResponse statusResponse = userProvisioningRequestSpy.createUser(callbackHost);
        assertNotNull(statusResponse);
        assertNotNull(statusResponse.getReferenceId());
        assertEquals(statusResponse.getReferenceId(), expectedStatusResponse.getReferenceId());
    }

    @Test
    @DisplayName("Create User Test Failure")
    void createUserTestFailure() throws MoMoException {
        UserProvisioningRequest userProvisioningRequestSpy = spy(new UserProvisioningRequest());
        
        CallbackHost callbackHost = new CallbackHost("");
        doThrow(MoMoException.class).when(userProvisioningRequestSpy).createUser(callbackHost);
        assertThrows(MoMoException.class, ()->userProvisioningRequestSpy.createUser(callbackHost));
    }

    @Test
    @DisplayName("Get User Details Test Success")
    void getUserDetailsTestSuccess() throws MoMoException {
        UserProvisioningRequest userProvisioningRequestSpy = spy(new UserProvisioningRequest());
        
        ApiUser expectedApiUser = getExpectedApiUser();
        doReturn(expectedApiUser).when(userProvisioningRequestSpy).getUserDetails(REFERENCE_ID_PARAMETER);
        
        ApiUser apiUser = userProvisioningRequestSpy.getUserDetails(REFERENCE_ID_PARAMETER);
        assertNotNull(apiUser);
        assertNotNull(apiUser.getProviderCallbackHost());
        assertEquals(apiUser.getProviderCallbackHost(), expectedApiUser.getProviderCallbackHost());
    }

    @Test
    @DisplayName("Get User Details Test Failure")
    void getUserDetailsTestFailure() throws MoMoException {
        UserProvisioningRequest userProvisioningRequestSpy = spy(new UserProvisioningRequest());
        
        doThrow(MoMoException.class).when(userProvisioningRequestSpy).getUserDetails(REFERENCE_ID_PARAMETER);
        assertThrows(MoMoException.class, ()->userProvisioningRequestSpy.getUserDetails(REFERENCE_ID_PARAMETER));
    }

    @Test
    @DisplayName("Create Api Key Test Success")
    void createApiKeyTestSuccess() throws MoMoException {
        UserProvisioningRequest userProvisioningRequestSpy = spy(new UserProvisioningRequest());
        
        ApiKey expectedApiKey = getExpectedApiKey();
        doReturn(expectedApiKey).when(userProvisioningRequestSpy).createApiKey(REFERENCE_ID_PARAMETER);
        
        ApiKey apiKey = userProvisioningRequestSpy.createApiKey(REFERENCE_ID_PARAMETER);
        assertNotNull(apiKey);
        assertNotNull(apiKey.getApiKey());
        assertEquals(apiKey.getApiKey(), expectedApiKey.getApiKey());
    }

    @Test
    @DisplayName("Create Api Key Test Failure")
    void createApiKeyTestFailure() throws MoMoException {
        UserProvisioningRequest userProvisioningRequestSpy = spy(new UserProvisioningRequest());
        
        doThrow(MoMoException.class).when(userProvisioningRequestSpy).createApiKey(REFERENCE_ID_PARAMETER);
        assertThrows(MoMoException.class, ()->userProvisioningRequestSpy.createApiKey(REFERENCE_ID_PARAMETER));
    }

    private StatusResponse getExpectedStatusResponse(boolean haveReferenceId) {
        StatusResponse statusResponse = new StatusResponse();
        if(haveReferenceId){
            statusResponse.setReferenceId(REFERENCE_ID_RETURNED);
        }
        statusResponse.setStatus(true);
        return statusResponse;
    }
    
    private ApiUser getExpectedApiUser(){
        ApiUser apiUser = new ApiUser("webhook.site");
        apiUser.setTargetEnvironment("sandbox");
        return apiUser;
    }
    
    private ApiKey getExpectedApiKey(){
        ApiKey apiKey = new ApiKey();
        apiKey.setApiKey(UUID.randomUUID().toString());
        return apiKey;
    }
}
