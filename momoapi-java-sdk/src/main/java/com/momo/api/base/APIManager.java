package com.momo.api.base;

import com.momo.api.base.constants.API;
import com.momo.api.base.constants.Constants;
import com.momo.api.base.util.StringUtils;
import com.momo.api.constants.Environment;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

/**
 *
 * Class APIManager
 */
public final class APIManager {

    // Used for dynamic configuration
    private Map<String, String> configurationMap;

    // Dynamic header map
    private Map<String, String> headersMap;

    // API URL
    private URL url;

    // API resource URL
    private String resourcePoint;

    // API payload
    private String payLoad;

    /**
     * *
     * Constructor with dynamic configuration
     *
     * @param configurationMap
     */
    public APIManager(Map<String, String> configurationMap) {
        this.configurationMap = SDKUtil.combineDefaultMap(configurationMap);
    }

    /**
     * *
     * Constructor with dynamic header
     *
     * @param configurationMap
     * @param headersMap
     */
    public APIManager(Map<String, String> configurationMap, Map<String, String> headersMap) {
        this(configurationMap);
        this.headersMap = (headersMap == null) ? Collections.<String, String>emptyMap() : headersMap;
    }

    /**
     * *
     * Returns API endpoint
     *
     * @return
     */
    public String getResourceEndPoint() {
        String endPoint = null;
        try {
            endPoint = getAPIBaseURL().toURI() + resourcePoint;
        } catch (Exception e) {

        }
        return endPoint;
    }

    /**
     * *
     * Returns API base URL
     *
     * @return
     * @throws Exception
     */
    public URL getAPIBaseURL() throws Exception {
        if (url == null) {
            String mode = this.configurationMap.get(Constants.MODE);
            String urlString = this.configurationMap.get(Constants.ENDPOINT);
            
            if (StringUtils.isNullOrEmpty(urlString)) {
                if (Environment.SANDBOX.name().equalsIgnoreCase(mode)) {
                    urlString = API.SANDBOX_URL;
                } else if (Environment.PRODUCTION.name().equalsIgnoreCase(mode)) {
                    urlString = API.PRODUCTION_URL;
                }
            }

            if (urlString == null || urlString.trim().length() <= 0) {
                //TODO the Exception can be changed to MoMoException
                //TODO the thrown exception is not handled in the subsequent method
                throw new Exception("Could not find API endpoint");
            }

            url = new URL(urlString);
        }
        return url;
    }

    /**
     * *
     * Returns HTTP header map
     *
     * @return
     */
    public Map<String, String> getHeaderMap() {
        return getProcessedHeaderMap();
    }

    /**
     * *
     * Returns configured HTTP header map
     *
     * @return
     */
    protected Map<String, String> getProcessedHeaderMap() {
        return this.headersMap;
    }

    /**
     * *
     * Returns configuration map
     *
     * @return
     */
    public Map<String, String> getConfigurationMap() {
        return this.configurationMap;
    }

    /**
     * *
     *
     * @return
     */
    public String getResourcePoint() {
        return resourcePoint;
    }

    /**
     *
     * @param resourcePoint
     */
    public void setResourcePoint(String resourcePoint) {
        this.resourcePoint = resourcePoint;
    }

    /**
     * *
     * Set payload
     *
     * @param payLoad
     */
    public void setPayLoad(String payLoad) {
        this.payLoad = payLoad;
    }

    /**
     * *
     * Returns payload
     *
     * @return
     */
    public String getPayLoad() {
        return this.payLoad;
    }
}
