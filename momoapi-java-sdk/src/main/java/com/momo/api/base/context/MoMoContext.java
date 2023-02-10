package com.momo.api.base.context;

import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.model.AccessToken;
import com.momo.api.constants.Environment;
import java.util.Map;

/**
 *
 * Interface MoMoContext
 */
public interface MoMoContext {
            
    public AccessToken getRefreshToken() throws MoMoException;
    
    public AccessToken fetchAccessToken() throws MoMoException;
    
    public MoMoContext addHTTPHeader(String key, String value);
    
    public String getHTTPHeader(String key);
    
    public Map<String, String> getHTTPHeaders();
    
    public Map<String, String> getConfigurationMap();
    
    public String getCallBackUrl();
    
    public MoMoContext setMode(Environment mode);
    
    //TODO this method is implemented but not yet called from anywhere. 
    //Make sure targetEnvironment need to be passed as parameter and call the method from appropriately after passing in the targetEnvironment value to it.
    public MoMoContext setTargetEnvironment(String targetEnvironment);
}
