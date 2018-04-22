package com.framework.proxy;

import com.framework.annotation.After;
import com.framework.annotation.Before;
import com.framework.util.AnnotationUtil;
import com.framework.util.LoadConfig;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;

public class MyProxy implements MethodInterceptor{

    public Enhancer enhancer = new Enhancer();

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        Set<Class<?>> cons = AnnotationUtil.getClass(LoadConfig.getPath(),
                "com.framework.annotation.Aspect");
        Iterator<Class<?>> it = cons.iterator();
        while (it.hasNext()){
            Class<?> cls = it.next();
            if(cls.isInstance(o)){
                Method[] methods = cls.getMethods();
                for (Method m : methods){
                    Before before = m.getAnnotation(Before.class);
                    if (before != null){
                        Object obj = cls.newInstance();
                        m.invoke(obj,objects);
                        break;
                    }
                }
            }
        }
        Object result = methodProxy.invokeSuper(o,objects);
        it = cons.iterator();
        while (it.hasNext()){
            Class<?> cls = it.next();
            if (cls.isInstance(o)){
                Method[] methods = cls.getDeclaredMethods();
                for (Method m : methods){
                    After after = m.getAnnotation(After.class);
                    if (after != null){
                        Object obj = cls.newInstance();
                        m.invoke(obj,objects);
                    }
                }
            }
        }
        return result;
    }
}
