package com.framework.proxy;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

public class ProxyInstance {

    public static Object getAuthInstanceByFilter(Class bean, MyProxy myProxy) {
        Enhancer en = new Enhancer();
        en.setSuperclass(bean);
        en.setCallbacks(new Callback[]{myProxy, NoOp.INSTANCE});
        en.setCallbackFilter(new ProxyFilter());
        return en.create();
    }
}
