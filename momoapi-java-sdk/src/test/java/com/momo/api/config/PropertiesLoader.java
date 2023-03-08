package com.momo.api.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/***
 * Class PropertiesLoader
 */
public class PropertiesLoader {

    Properties properties;
    
    /***
     * Constructor for loading properties file
     */
    public PropertiesLoader() {
        try (InputStream input = new FileInputStream("src/test/resources/config.properties")) {
        	this.properties = new Properties();
        	this.properties.load(input);
        } catch (IOException io) {
//            io.printStackTrace();
        }
    }
    
    /***
     *
     * @param key
     * @return
     */
    public String get(String key){
        return this.properties.getProperty(key);
    }
}
