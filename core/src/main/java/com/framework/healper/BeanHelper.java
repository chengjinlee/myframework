package com.framework.healper;

import com.framework.annotation.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class BeanHelper {

    private static final Map<Class<?>,Object> BEAN_MAP= new HashMap<>();
    static {
        Set<Class<?>> beanSet = ClassHelper.getBeanClassSet();
    }

    /**
     * 获取Bean映射
     * @return
     */
    public static Map<Class<?>, Object> getBeanMap() {
        return BEAN_MAP;
    }

    /**
     * 获取Bean实例
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> cls){
        if (!BEAN_MAP.containsKey(cls)){
            throw new RuntimeException("can not get bean by class"+ cls);
        }
        return (T) BEAN_MAP.get(cls);
    }

    public static void main(String[] args) {
        getBean(Service.class);
    }
}
