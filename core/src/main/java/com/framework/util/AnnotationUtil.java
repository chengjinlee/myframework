package com.framework.util;

import org.reflections.Reflections;

import java.util.Set;

public class AnnotationUtil {

    public static Set<Class<?>> getClass(String pack, String AnnotationName) throws ClassNotFoundException {

        Class cl = Class.forName(AnnotationName);
        Reflections reflections = new Reflections(pack);
        Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(cl);
        return classSet;
    }
}
