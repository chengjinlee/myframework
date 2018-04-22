package com.framework.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器接口
 */
public interface Interceptor {
    boolean preHandle(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
