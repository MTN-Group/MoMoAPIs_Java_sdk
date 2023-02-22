package base;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * Class SDKClient
 */
public class SDKClient {

    private static Properties properties;

    static {
        try ( InputStream input = new FileInputStream("resources/config.properties")) {
            properties = new Properties();
            properties.load(input);
        } catch (IOException io) {
        }
    }

    /**
     * *
     *
     * @param key
     * @return
     */
    public static String get(String key) {
        return properties.getProperty(key);
    }

    public static String getSampleMSISDN() {
        return "23423423450";
    }
}
