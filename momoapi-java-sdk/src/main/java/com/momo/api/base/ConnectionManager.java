package com.momo.api.base;

import javax.net.ssl.SSLContext;

/***
 * Class ConnectionManager
 */
public final class ConnectionManager {
    // Singleton Instance
    private static ConnectionManager instance;

    // SSL
    private SSLContext customSslContext;

    // Private Constructor
    private ConnectionManager() { }

    /***
     * Singleton accessor method
     *
     * @return
     */
    public static ConnectionManager getInstance() {
        if (instance == null) {
            synchronized (ConnectionManager.class) {
                if (instance == null) {
                    instance = new ConnectionManager();
                }
            }
        }
        return instance;
    }

    /***
     *
     * @return
     */
    public HttpConnection getConnection() {
        return (customSslContext != null) ? new DefaultHttpConnection(customSslContext) : new DefaultHttpConnection();
    }
}

