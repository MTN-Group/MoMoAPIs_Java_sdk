package com.momo.api.base;

import com.momo.api.base.constants.Constants;
import com.momo.api.base.exception.SSLConfigurationException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;

/***
 * Class SSLUtil
 */
public abstract class SSLUtil {
    private static final Map<String, String> CONFIG_MAP;

    static {
        CONFIG_MAP = SDKUtil.combineDefaultMap(ConfigManager.getInstance().getConfigurationMap());
    }

    /***
     *
     * @param keyManagers
     * @return
     * @throws SSLConfigurationException
     */
    public static SSLContext getSSLContext(KeyManager[] keyManagers) throws SSLConfigurationException {
        try {
            SSLContext ctx;
            String protocol = CONFIG_MAP.get(Constants.SSLUTIL_PROTOCOL);
            try {
                ctx = SSLContext.getInstance("TLSv1.2");
            } catch (NoSuchAlgorithmException e) {
                ctx = SSLContext.getInstance(protocol);
            }
            ctx.init(keyManagers, null, null);
            return ctx;
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            throw new SSLConfigurationException(e.getMessage(), e);
        }
    }
}

