package com.mmall.common;

import com.mmall.model.SysUser;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Wgs
 * @version 1.0
 * @create：2020/08/05
 */
public class RequestHolder {
    private static final ThreadLocal<SysUser> userHolder = new ThreadLocal<>();
    private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<>();

    public static void add(SysUser sysUser) {
        userHolder.set(sysUser);
    }

    public static void add(HttpServletRequest request) {
        requestHolder.set(request);
    }

    public static SysUser getUser() {
        return userHolder.get();
    }

    public static HttpServletRequest getRequest() {
        return requestHolder.get();
    }

    public static void remove(){
        requestHolder.remove();
        userHolder.remove();
    }

}
