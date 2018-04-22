package com.framework.healper;

import java.util.Collection;
import java.util.Map;

public class IocHelper {

    static {
        //获取所有的Bean类与Bean实例之间的映射关系
        Map<Class<?>,Object> beanMap = BeanHelper.getBeanMap();

    }
}
