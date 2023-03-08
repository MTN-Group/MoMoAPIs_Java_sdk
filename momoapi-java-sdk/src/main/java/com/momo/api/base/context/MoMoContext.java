package com.momo.api.base.context;

import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.model.AccessToken;
import com.momo.api.base.model.Oauth2Token;
import com.momo.api.constants.Environment;
import java.util.Map;

/**
 *
 * Interface MoMoContext
 */
public interface MoMoContext {
            
    public AccessToken getRefreshToken() throws MoMoException;
    
    public Oauth2Token getRefreshOauth2Token(String auth_req_id) throws MoMoException;
    
    public AccessToken fetchAccessToken() throws MoMoException;
    
    public Oauth2Token fetchOauth2Token(String auth_req_id) throws MoMoException;
    
    public MoMoContext addHTTPHeader(String key, String value);
    
    public String getHTTPHeader(String key);
    
    public Map<String, String> getHTTPHeaders();
    
    public Map<String, String> getConfigurationMap();
    
    public String getCallBackUrl();
    
    public MoMoContext setMode(Environment mode);
    
    public MoMoContext setTargetEnvironment(String targetEnvironment);
}
