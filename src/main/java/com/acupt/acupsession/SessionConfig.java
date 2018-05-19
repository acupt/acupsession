package com.acupt.acupsession;

import javax.servlet.FilterConfig;

/**
 * Created by liujie on 2018/5/18.
 */
public class SessionConfig {

    // cookie
    private static String cookieSessionid = "acupsession_id";//cookie中存储sessionID的键名

    // redis
    private static String redisHost = "localhost";
    private static String redisPassword = "000000";
    private static int redisPort = 6379;
    private static int redisTimeout = 2000;//seconds
    private static String redisPrefix = "acupsession";//redis中session管理相关的key统一前缀

    //session
    private static int sessionTimeout = 30;//minute，session过期时间，超过时间未访问的session将失效

    public static void read(FilterConfig filterConfig) {
        cookieSessionid = getInitParameter(filterConfig, "cookieSessionid", cookieSessionid);

        redisHost = getInitParameter(filterConfig, "redisHost", redisHost);
        redisPassword = getInitParameter(filterConfig, "redisPassword", redisPassword);
        redisPort = Integer.parseInt(getInitParameter(filterConfig, "redisPort", redisPort));
        redisTimeout = Integer.parseInt(getInitParameter(filterConfig, "redisTimeout", redisTimeout));

        sessionTimeout = Integer.parseInt(getInitParameter(filterConfig, "sessionTimeout", sessionTimeout));
        redisPrefix = getInitParameter(filterConfig, "acupsession", redisPrefix);
    }

    private static String getInitParameter(FilterConfig filterConfig, String name, Object defaultValue) {
        String v = filterConfig.getInitParameter(name);
        if (v == null || v.equals("")) {
            return defaultValue.toString();
        }
        return v;
    }

    public static String getCookieSessionid() {
        return cookieSessionid;
    }

    public static String getRedisHost() {
        return redisHost;
    }

    public static int getRedisPort() {
        return redisPort;
    }

    public static String getRedisPassword() {
        return redisPassword;
    }

    public static int getRedisTimeout() {
        return redisTimeout;
    }

    public static int getSessionTimeout() {
        return sessionTimeout;
    }

    public static int getSessionMaxInactiveInterval() {
        return getSessionTimeout() * 60;
    }

    public static String getRedisPrefix() {
        return redisPrefix;
    }
}
