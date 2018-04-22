package com.framework.healper;

import com.framework.config.ConfigConstant;
import com.framework.util.PropsUtil;

import java.util.Properties;

public class ConfigHelper {

    private static final Properties CONFIG_PROPS =
            PropsUtil.loadProps(ConfigConstant.CONFIG_FILE);

    public static String getAppBasePackage(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.APP_BASE_PACKAGE);
    }

}
