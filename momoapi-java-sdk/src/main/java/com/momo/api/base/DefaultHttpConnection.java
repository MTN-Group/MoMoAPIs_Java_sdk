package com.momo.api.base;

import com.momo.api.base.constants.Constants;
import com.momo.api.base.exception.SSLConfigurationException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

/***
 * Class DefaultHttpConnection
 */
public class DefaultHttpConnection extends HttpConnection {
    // SSL
    private SSLContext sslContext;

    /***
     *
     * @param clientConfiguration
     * @throws IOException
     */
    @Override
    public void createAndConfigureHttpConnection(HttpConfiguration clientConfiguration) throws IOException {
        this.config = clientConfiguration;
        URL url = new URL(this.config.getEndPointUrl());

        this.connection = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY);

        if (this.connection instanceof HttpsURLConnection) {
            ((HttpsURLConnection) this.connection).setSSLSocketFactory(this.sslContext.getSocketFactory());
        }

        System.setProperty(Constants.HTTP_CONNECTION_MAX_CONNECTION, String.valueOf(this.config.getMaxHttpConnection()));
        System.setProperty(Constants.ENABLE_HTTP_ERROR_BUFFERING, "true");

        this.connection.setDoInput(true);
        this.connection.setDoOutput(true);
        this.connection.setConnectTimeout(this.config.getConnectionTimeout());
        this.connection.setReadTimeout(this.config.getReadTimeout());
    }

    /***
     *
     */
    public DefaultHttpConnection() {
        try {
            sslContext = SSLUtil.getSSLContext(null);
        } catch (SSLConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    /***
     *
     * @param sslContext
     */
    public DefaultHttpConnection(SSLContext sslContext) {
        this.sslContext = sslContext;
    }
}

