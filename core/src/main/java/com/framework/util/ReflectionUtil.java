package com.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtil.class);

    /**
     * 创建实例
     * @param cls
     * @return
     */
    public static Object newInstance(Class<?> cls){
        Object instance;
        try {
            instance = cls.newInstance();
        }catch (Exception e){
            LOGGER.error("new instance failed");
            throw new RuntimeException(e);
        }
        return instance;
    }

    /**
     * 创建实例(根据类名)
     * @param className
     * @return
     */
    public static Object newInstance(String className){

        Class<?> cls = ClassUtil.loadClass(className);
        return newInstance(cls);
    }

    /**
     * 反射调用方法
     * @param obj
     * @param m
     * @param args
     * @return
     */
    public static Object invokeMethod(Object obj ,Method m, Object... args){

        Object result;
        try{
            m.setAccessible(true);
            result = m.invoke(obj,args);
        }catch (Exception e){
            LOGGER.error("invoke failed",e);
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 设置成员变量的值
     * @param obj
     * @param field
     * @param val
     */
    public static void setFiled(Object obj, Field field,Object val){

        try {
            field.setAccessible(true);
            field.set(obj,val);
        }catch (Exception e){
            LOGGER.error("set filed failed",e);
            throw new RuntimeException(e);
        }
    }
}
