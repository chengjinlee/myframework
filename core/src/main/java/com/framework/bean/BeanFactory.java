package com.framework.bean;

import com.framework.annotation.Aspect;
import com.framework.proxy.MyProxy;
import com.framework.proxy.ProxyInstance;
import com.framework.util.AnnotationUtil;
import com.framework.util.LoadConfig;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Bean 容器 使用HashMap
 */
public class BeanFactory {

    private static Map<String,Map<String,Object>> config = new HashMap<>();
    private static BeanFactory beanFactory = new BeanFactory();

    public static BeanFactory getBeanFactory(){
        Set cons = null;
        try {
            cons = AnnotationUtil.getClass(LoadConfig.getPath(),"com.framework.annotation.AutoWired");
            Iterator e = cons.iterator();
            while (e.hasNext()){
                Class cls = (Class) e.next();
                HashMap beanConfig = new HashMap();
                beanConfig.put("class", cls.getName());
                config.put(cls.getSimpleName(),beanConfig);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return beanFactory;
    }

    private BeanFactory(){
    }

    public static Object getBean(String beanId){
        HashMap beanconfig = (HashMap) config.get(beanId);
        String className = (String) beanconfig.get("class");
        Class bean = null;

        try {
            bean = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Object instance = null;
        try{
            if (bean.getAnnotation(Aspect.class) == null){
                instance = bean.newInstance();
            }else {
                instance = ProxyInstance.getAuthInstanceByFilter(bean, new MyProxy());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Field[] e = bean.getDeclaredFields();
        int n = e.length;
        for (int i=0;i<n;++i){
            Field field = e[i];
            String fieldName = field.getName();
            Method method = null;
            try{
                method = bean.getDeclaredMethod("set"+fieldName.substring(0,1)
                        .toUpperCase()+fieldName.substring(1), new Class[]{field.getType()});
                method.invoke(instance, new Object[]{field.getType()});
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return instance;
    }
}
