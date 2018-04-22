package com.framework.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LoadConfig {

    public static String getPath(){
        Properties properties = new Properties();
        String key = "packagePath";
        try {
            InputStream is = JDBCUtil.class.getClassLoader().getResourceAsStream("application.properties");
            properties.load(is);
            String value = properties.getProperty(key);
            return value;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
