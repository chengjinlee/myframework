package com.framework.proxy;

import com.framework.annotation.Pointcut;
import net.sf.cglib.proxy.CallbackFilter;

import java.lang.reflect.Method;

public class ProxyFilter implements CallbackFilter{

    @Override
    public int accept(Method method) {
        if(method.getDeclaredAnnotation(Pointcut.class)!=null){
            return 0;
        }else{
            return 1;
        }
    }
}
