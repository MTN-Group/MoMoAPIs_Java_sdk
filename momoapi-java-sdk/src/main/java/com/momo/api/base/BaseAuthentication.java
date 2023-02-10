package com.momo.api.base;

import com.momo.api.base.constants.API;
import com.momo.api.base.constants.Constants;
import com.momo.api.base.util.StringUtils;
import com.momo.api.constants.Environment;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Abstract Class BaseAuthentication
 */
public abstract class BaseAuthentication {

    public final Map<String, String> headers = new HashMap<>();

    public Map<String, String> configurationMap;

    /**
     * BaseAuthentication Constructor
     *
     */
    public BaseAuthentication() {
        super();
    }

    /**
     * Add a configuration to list of configurations
     *
     * @param key
     * @param value
     * @return
     */
    public BaseAuthentication addConfiguration(String key, String value) {
        if (this.configurationMap == null) {
            this.configurationMap = new HashMap<>();
        }
        this.configurationMap.put(key, value);
        return this;
    }

    /**
     * Adds configurations to list of configurations
     *
     * @param configurations
     * @return
     */
    public BaseAuthentication addConfigurations(Map<String, String> configurations) {
        if (this.configurationMap == null) {
            this.configurationMap = new HashMap<>();
        }
        this.configurationMap.putAll(configurations);
        return this;
    }

    /**
     * Returns HttpConfiguration after setting the EndPointUrl
     *
     * @param url
     * @return
     * @throws MalformedURLException
     */
    protected HttpConfiguration getOAuthHttpConfiguration(String url) throws MalformedURLException {
        HttpConfiguration httpConfiguration = new HttpConfiguration();
        httpConfiguration.setHttpMethod(Constants.HTTP_CONFIG_DEFAULT_HTTP_METHOD);

        Environment mode = Environment.valueOf(this.configurationMap.get(Constants.MODE));

        // Default to Endpoint param.
        String endPointUrl = this.configurationMap.get(Constants.OAUTH_ENDPOINT);

        if (StringUtils.isNullOrEmpty(endPointUrl)) {
            switch (mode) {
                case SANDBOX:
                    endPointUrl = API.SANDBOX_URL + url;
                    break;
                case PRODUCTION:
                    endPointUrl = API.PRODUCTION_URL + url;
                    break;
                default:
                    endPointUrl = "";
            }
        }

        // If none of the option works, throw exception.
        if (endPointUrl == null || endPointUrl.trim().length() <= 0) {
            throw new MalformedURLException(String.format("Not configured to %s/%s", Environment.SANDBOX, Environment.PRODUCTION));
        }

        httpConfiguration.setEndPointUrl(endPointUrl);

        return httpConfiguration;
    }

    /**
     * Returns list of configurations
     *
     * @return
     */
    public Map<String, String> getConfigurations() {
        return this.configurationMap;
    }

    /**
     * Returns HTTP headers
     *
     * @return
     */
    public Map<String, String> getHeaders() {
        return this.headers;
    }

    /**
     * Returns the header value
     *
     * @param key
     * @return
     */
    public String getHeader(String key) {
        return this.headers.get(key);
    }

    /**
     * Add header value to existing list of headers
     *
     * @param key
     * @param value
     * @return
     */
    public BaseAuthentication addHeader(String key, String value) {
        this.headers.put(key, value);
        return this;
    }
}
