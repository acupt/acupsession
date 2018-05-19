package com.acupt.acupsession;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by liujie on 2018/5/8.
 */
public class RedisSessionFilter implements Filter {

    private SessionStore sessionStore;

    public void init(FilterConfig filterConfig) throws ServletException {
        SessionConfig.read(filterConfig);
        JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), SessionConfig.getRedisHost(),
                SessionConfig.getRedisPort(), SessionConfig.getRedisTimeout(), SessionConfig.getRedisPassword());
        sessionStore = new SessionStore(jedisPool);
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequestWrapper requestWrapper =
                new HttpServletRequestWrapper((HttpServletRequest) request, (HttpServletResponse) response, sessionStore);
        chain.doFilter(requestWrapper, response);
        sessionStore.save(requestWrapper.getSession().getId());
    }

    public void destroy() {

    }

}
