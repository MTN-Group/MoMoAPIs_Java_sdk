package com.momo.api.base;

import com.momo.api.base.constants.Constants;
import com.momo.api.base.util.ResourceLoader;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessControlException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * *
 * Class ConfigManager
 */
public final class ConfigManager {

    // ConfigManager Reference
    private static ConfigManager conf;

    // Properties
    private Properties properties;

    // Configuration Map
    private Map<String, String> mapView = null;

    // Default Configuration Map
    private static Map<String, String> defaultMapView;

    // Property load flag
    private boolean propertyLoaded = false;

    // Default Properties
    private static final Properties DEFAULT_PROPERTIES;

    // Initialize DEFAULT_PROPERTIES
    static {
        DEFAULT_PROPERTIES = new Properties();
        defaultMapView = new HashMap<>();
        for (Object object : DEFAULT_PROPERTIES.keySet()) {
            defaultMapView.put(object.toString().trim(), DEFAULT_PROPERTIES
                    .getProperty(object.toString()).trim());
        }
    }

    /**
     * *
     * Private constructor
     *
     */
    private ConfigManager() {
        ResourceLoader resourceLoader = new ResourceLoader(Constants.DEFAULT_CONFIGURATION_FILE);
        properties = new Properties();

        try {
            InputStream inputStream = resourceLoader.getInputStream();
            properties.load(inputStream);
        } catch (IOException | AccessControlException e) {
        } finally {
            setPropertyLoaded(true);
        }
    }

    /**
     * *
     *
     * @throws MoMoException
     * @return 
     */
    public static ConfigManager getInstance() {
        if (conf == null) {
            synchronized (ConfigManager.class) {
                if (conf == null) {
                    conf = new ConfigManager();
                }
            }
        }
        return conf;
    }

    /**
     * *
     *
     * @return
     */
    public static Map<String, String> getDefaultSDKMap() {
        return new HashMap<>(defaultMapView);
    }

    /**
     * *
     *
     * @return
     */
    public Map<String, String> getConfigurationMap() {
        if (mapView == null) {
            synchronized (DEFAULT_PROPERTIES) {
                mapView = new HashMap<>();
                if (properties != null) {
                    for (Object object : properties.keySet()) {
                        mapView.put(object.toString().trim(), properties
                                .getProperty(object.toString()).trim());
                    }
                }
            }
        }
        return new HashMap<>(mapView);
    }

    /**
     * *
     *
     * @param propertyLoaded
     */
    private void setPropertyLoaded(boolean propertyLoaded) {
        this.propertyLoaded = propertyLoaded;
    }
}
